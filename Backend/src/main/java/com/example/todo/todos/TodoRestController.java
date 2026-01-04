package com.example.todo.todos;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/todos")
public class TodoRestController {
    
    private final TodoService todoService;
    
    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    // GET /api/v1/todos - Get ALL todos
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    // GET /api/v1/todos/user/{userId} - Get todos for specific user
    @GetMapping("/user/{userId}")
    public List<Todo> getTodosByUser(@PathVariable Integer userId) {
        return todoService.getTodosByUserId(userId);
    }
    
    // GET /api/v1/todos/{id} - Get single todo by ID
    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
        return todoService.getTodoById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }
    
    // POST /api/v1/todos?userId=1 - Create todo for a user
    @PostMapping
    public Todo createTodo(@RequestParam Integer userId, @RequestBody Todo todo) {
        return todoService.createTodo(userId, todo);
    }
    
    // PUT /api/v1/todos/{id} - Update existing todo
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Integer id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }
    
    // DELETE /api/v1/todos/{id} - Delete todo
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Integer id) {
        todoService.deleteTodo(id);
    }
    
    // GET /api/v1/todos/{id}/pdf - Export single todo to PDF
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportTodoToPdf(@PathVariable Integer id) {
        byte[] pdfBytes = todoService.exportTodoToPdf(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "todo_" + id + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes);
    }
    
    // GET /api/v1/todos/user/{userId}/pdf - Export all todos for user to PDF
    @GetMapping("/user/{userId}/pdf")
    public ResponseEntity<byte[]> exportAllTodosToPdf(@PathVariable Integer userId) {
        byte[] pdfBytes = todoService.exportAllTodosToPdf(userId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "vsi_todos_" + userId + ".pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes);
    }
    
    // POST /api/v1/todos/{id}/attachment - Upload attachment for a todo
    @PostMapping("/{id}/attachment")
    public ResponseEntity<String> uploadAttachment(
        @PathVariable Integer id,
        @RequestParam("file") MultipartFile file) {
    
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        
        // Validate file size (5MB max)
        long maxSize = 5 * 1024 * 1024; // 5MB in bytes
        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest()
                .body("File size exceeds 5MB limit");
        }
        
        // Validate file type (only PNG, JPG, PDF)
        String contentType = file.getContentType();
        if (contentType == null || 
            (!contentType.equals("image/png") && 
             !contentType.equals("image/jpeg") && 
             !contentType.equals("application/pdf"))) {
            return ResponseEntity.badRequest()
                .body("Invalid file type. Only PNG, JPG, and PDF files are allowed");
        }
        
        try {
            String result = todoService.saveAttachment(id, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Failed to upload attachment: " + e.getMessage());
        }
    }

    // DELETE /api/v1/todos/{todoId}/attachment/{attachmentId} - Delete attachment
    @DeleteMapping("/{todoId}/attachment/{attachmentId}")
    @CrossOrigin
    public ResponseEntity<String> deleteAttachment(
        @PathVariable Integer todoId,
        @PathVariable Integer attachmentId) {
        
        try {
            todoService.deleteAttachment(todoId, attachmentId);
            return ResponseEntity.ok("Attachment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Failed to delete attachment: " + e.getMessage());
        }
    }
}
