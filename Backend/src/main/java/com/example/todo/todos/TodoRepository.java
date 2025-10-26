package com.example.todo.todos;

import org.springframework.data.repository.CrudRepository;

public interface  TodoRepository extends CrudRepository<Todo, Integer> {

}