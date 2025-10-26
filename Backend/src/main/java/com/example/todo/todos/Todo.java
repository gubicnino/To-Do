package com.example.todo.todos;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String title;
    private String description;
}