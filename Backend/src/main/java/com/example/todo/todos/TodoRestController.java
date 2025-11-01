package com.example.todo.todos;

import java.util.List;

import org.springframework.web.bind.annotation.*;

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
}