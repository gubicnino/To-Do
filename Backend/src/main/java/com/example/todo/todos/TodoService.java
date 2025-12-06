package com.example.todo.todos;

import java.util.List;
import com.example.todo.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.user.UserRepository;

import java.util.Optional;

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
}