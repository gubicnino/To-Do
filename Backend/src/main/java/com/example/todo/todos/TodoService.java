package com.example.todo.todos;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
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
                existingTodo.setTitle(updatedTodo.getTitle());
                existingTodo.setDescription(updatedTodo.getDescription());
                existingTodo.setCompleted(updatedTodo.isCompleted());
                existingTodo.setPriority(updatedTodo.getPriority());
                existingTodo.setDueDate(updatedTodo.getDueDate());
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
}