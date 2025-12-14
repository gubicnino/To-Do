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
}