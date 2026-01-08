package com.example.todo.todos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    
    public TodoService(TodoRepository todoRepository, UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }
    
    // Get all todos
    public List<Todo> getAllTodos() {
        return (List<Todo>) todoRepository.findAll();
    }
    // Get all todos for a specific user
    public List<Todo> getTodosByUserId(Integer userId) {
        return todoRepository.findByUserId(userId);
    }
    // Create todo for a specific user
    public Todo createTodo(Integer userId, Todo todo) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Set defaults if not provided
        if (todo.getPriority() == null) {
            todo.setPriority(Todo.Priority.MEDIUM);
        }
        
        todo.setUser(user);  // Link todo to user
        todo.setStartTime(LocalDateTime.now());
        todo.setEndTime(null);
        return todoRepository.save(todo);
    }
    // Get single todo by ID
    public Optional<Todo> getTodoById(Integer id) {
        return todoRepository.findById(id);
    }
    
    // Create new todo
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }
    
    // Update existing todo
    public Todo updateTodo(Integer id, Todo updatedTodo) {
        return todoRepository.findById(id)
            .map(existingTodo -> {
                // Shrani staro vrednost PRED spreminjanjem
                boolean wasCompleted = existingTodo.isCompleted();
                
                existingTodo.setTitle(updatedTodo.getTitle());
                existingTodo.setDescription(updatedTodo.getDescription());
                existingTodo.setCompleted(updatedTodo.isCompleted());
                existingTodo.setPriority(updatedTodo.getPriority());
                existingTodo.setDueDate(updatedTodo.getDueDate());
                
                // Preveri STARO vs NOVO vrednost
                if (updatedTodo.isCompleted() && !wasCompleted) {
                    existingTodo.setEndTime(LocalDateTime.now());
                } else if (!updatedTodo.isCompleted()) {
                    existingTodo.setEndTime(null);
                }
                
                return todoRepository.save(existingTodo);
            })
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }
    
    // Delete todo
    public void deleteTodo(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
    
    /**
     * EXPORTAMO EN TODO KAK PDF
     */
    public byte[] exportTodoToPdf(Integer id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Naslov
            document.add(new Paragraph("TODO Dokument")
                .setFontSize(20)
                .setBold());
            
            document.add(new Paragraph("\n"));
            
            // VSE TABELE
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}));
            table.setWidth(UnitValue.createPercentValue(100));
            
            table.addCell("Naslov:");
            table.addCell(todo.getTitle() != null ? todo.getTitle() : "N/A");
            
            table.addCell("Opis:");
            table.addCell(todo.getDescription() != null ? todo.getDescription() : "Ni opisa");
            
            table.addCell("Prioriteta:");
            table.addCell(todo.getPriority() != null ? todo.getPriority().toString() : "N/A");
            
            table.addCell("Stanje:");
            table.addCell(todo.isCompleted() ? "Zaključeno" : "Aktivno");
            
            table.addCell("Rok:");
            if (todo.getDueDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                table.addCell(todo.getDueDate().format(formatter));
            } else {
                table.addCell("Ni določen");
            }
            
            document.add(table);
            document.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    /**
     * EXPORTAMO VSE TODOJE KAK PDF
     */
    public byte[] exportAllTodosToPdf(Integer userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        if (todos.isEmpty()) {
            throw new RuntimeException("No todos found for user with id: " + userId);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            document.add(new Paragraph("Vsi TODO-ji")
                .setFontSize(20)
                .setBold());
            
            long completed = todos.stream().filter(Todo::isCompleted).count();
            long active = todos.size() - completed;
            
            document.add(new Paragraph(String.format("Skupno: %d | Aktivni: %d | Zaključeni: %d", 
                todos.size(), active, completed))
                .setFontSize(12));
            
            document.add(new Paragraph("\n"));
            
            // Vsak TODO kot tabela
            for (int i = 0; i < todos.size(); i++) {
                Todo todo = todos.get(i);
                
                document.add(new Paragraph((i + 1) + ". " + todo.getTitle())
                    .setFontSize(14)
                    .setBold());
                
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}));
                table.setWidth(UnitValue.createPercentValue(100));
                
                table.addCell("Opis:");
                table.addCell(todo.getDescription() != null ? todo.getDescription() : "Ni opisa");
                
                table.addCell("Prioriteta:");
                table.addCell(todo.getPriority() != null ? todo.getPriority().toString() : "N/A");
                
                table.addCell("Stanje:");
                table.addCell(todo.isCompleted() ? "✓ Zaključeno" : "○ Aktivno");
                
                table.addCell("Rok:");
                if (todo.getDueDate() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    table.addCell(todo.getDueDate().format(formatter));
                } else {
                    table.addCell("Ni določen");
                }
                
                document.add(table);
                document.add(new Paragraph("\n"));
            }
            
            document.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
        
        return baos.toByteArray();
    }
    // Save attachment for a todo
    public String saveAttachment(Integer todoId, MultipartFile file) throws IOException {
        // Check if todo exists
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + todoId));
        
        // Create uploads directory if it doesn't exist
        String uploadDir = "priloge/" + todoId;
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Get original filename and save the file
        String originalFilename = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(originalFilename);
        
        // Save the file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Create Attachment entity and save to database
        Attachment attachment = new Attachment();
        attachment.setFileName(originalFilename);
        attachment.setFilePath(filePath.toString());
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUploadDate(LocalDateTime.now());
        attachment.setTodo(todo);
        
        attachmentRepository.save(attachment);
        
        return "File uploaded successfully: " + originalFilename;
    }
    
    // Delete attachment
    public void deleteAttachment(Integer todoId, Integer attachmentId) throws IOException {
        Attachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));
        
        // Verify attachment belongs to the specified todo
        if (!attachment.getTodo().getId().equals(todoId)) {
            throw new RuntimeException("Attachment does not belong to this todo");
        }
        
        // Delete file from filesystem
        Path filePath = Paths.get(attachment.getFilePath());
        Files.deleteIfExists(filePath);
        
        // Delete from database
        attachmentRepository.delete(attachment);
    }

    // STATISTIKAA

    public Double getAverageTodoDuration(Integer userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        List<Todo> completedTodos = todos.stream()
            .filter(todo -> todo.isCompleted() && todo.getEndTime() != null && todo.getStartTime() != null)
            .toList();
        
        if (completedTodos.isEmpty()) {
            return null;
        }
        
        double totalHours = completedTodos.stream()
            .mapToDouble(todo -> {
                long seconds = java.time.Duration.between(todo.getStartTime(), todo.getEndTime()).getSeconds();
                return seconds / 3600.0;
            })
            .sum();
        
        return totalHours / completedTodos.size();
    }

    public Double getTotalTimeSpent(Integer userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        return todos.stream()
            .filter(todo -> todo.isCompleted() && todo.getEndTime() != null && todo.getStartTime() != null)
            .mapToDouble(todo -> {
                long seconds = java.time.Duration.between(todo.getStartTime(), todo.getEndTime()).getSeconds();
                return seconds / 3600.0;
            })
            .sum();
    }

    public Double getCompletionPercentage(Integer userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        if (todos.isEmpty()) {
            return 0.0;
        }
        
        long completedCount = todos.stream()
            .filter(Todo::isCompleted)
            .count();
        
        return (completedCount * 100.0) / todos.size();
    }

    public Long getActiveTasksCount(Integer userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        return todos.stream()
            .filter(todo -> !todo.isCompleted())
            .count();
    }
}
