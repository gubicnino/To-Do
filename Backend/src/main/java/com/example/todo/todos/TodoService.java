package com.example.todo.todos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    // Get all todos
    public List<Todo> getAllTodos() {
        return (List<Todo>) todoRepository.findAll();
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
}