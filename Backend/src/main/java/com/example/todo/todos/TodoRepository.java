package com.example.todo.todos;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface  TodoRepository extends CrudRepository<Todo, Integer> {
    List<Todo> findByUserId(Integer userId);
}