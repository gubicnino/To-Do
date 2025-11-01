package com.example.todo.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;



@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthRestController {
    
    private final UserRepository userRepository;

    public AuthRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest RegisterRequest) {
        if(userRepository.findByUsername(RegisterRequest.getUsername()).isPresent()){
            throw new RuntimeException("Username is already taken");
        }
        if(userRepository.findByEmail(RegisterRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email is already taken");
        }
        User user = new User();
        user.setUsername(RegisterRequest.getUsername());
        user.setEmail(RegisterRequest.getEmail());
        user.setPassword(RegisterRequest.getPassword());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new AuthResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail()
        ));
    }
    

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        return ResponseEntity.ok(new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        ));
    }
}