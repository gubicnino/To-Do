package com.example.todo.todos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lombok.Data;

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

    // TO VRNE VSE STIRE STATISTIKE ZA ANALIZO (getAnalytics)
    @GetMapping("/user/{userId}/analytics")
    public ResponseEntity<TodoAnalytics> getAnalytics(@PathVariable Integer userId) {
        TodoAnalytics analytics = new TodoAnalytics();
        analytics.setAverageDuration(todoService.getAverageTodoDuration(userId));
        analytics.setTotalTimeSpent(todoService.getTotalTimeSpent(userId));
        analytics.setCompletionPercentage(todoService.getCompletionPercentage(userId));
        analytics.setActiveTasksCount(todoService.getActiveTasksCount(userId));
        analytics.setTimeToCompleteAllTodos(todoService.getTimeToCompleteAllTodos(userId));
        analytics.setWorkDaysToCompleteAllTodos(todoService.getWorkDaysToCompleteAllTodos(userId));
        
        return ResponseEntity.ok(analytics);
    }

    
    @Data
    static class TodoAnalytics {
        private Double averageDuration;
        private Double totalTimeSpent;
        private Double completionPercentage;
        private Long activeTasksCount;
        private float timeToCompleteAllTodos;
        private int workDaysToCompleteAllTodos;
    }
    @GetMapping("/user/{userId}/analytics/pie")
public ResponseEntity<PieAnalytics> getPieAnalytics(@PathVariable Integer userId) {

    List<Todo> todos = todoService.getTodosByUserId(userId);

    long total = todos.size();
    long completed = todos.stream().filter(Todo::isCompleted).count();
    long active = total - completed;

    Map<String, Long> prioCounts = new HashMap<>();
    for (Todo t : todos) {
        String p = (t.getPriority() == null) ? "NONE" : t.getPriority().name();
        prioCounts.put(p, prioCounts.getOrDefault(p, 0L) + 1);
    }

    ArrayList<PieSlice> slices = new ArrayList<>();
    prioCounts.forEach((k, v) -> slices.add(new PieSlice(k, v)));

    PieAnalytics out = new PieAnalytics();
    out.setTotal(total);
    out.setCompleted(completed);
    out.setActive(active);
    out.setByPriority(slices);

    return ResponseEntity.ok(out);
}

@Data
static class PieAnalytics {
    private long total;
    private long completed;
    private long active;
    private List<PieSlice> byPriority;
}

@Data
static class PieSlice {
    private String name;
    private long value;

    public PieSlice(String name, long value) {
        this.name = name;
        this.value = value;
    }
}
}
