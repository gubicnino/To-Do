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
    
    // GET /api/v1/todos - Get all todos
    @GetMapping
    public List<Todo> getAllTodos() {
        return (List<Todo>) todoService.getAllTodos();
    }
    
    // GET /api/v1/todos/{id} - Get single todo by ID
    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
        return todoService.getTodoById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }
    
    // POST /api/v1/todos - Create new todo
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
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