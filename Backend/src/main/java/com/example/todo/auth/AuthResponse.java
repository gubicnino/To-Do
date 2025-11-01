package com.example.todo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Integer id;
    private String username;
    private String email;
}
