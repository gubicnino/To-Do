package com.example.todo.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    // Get single user by ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Create new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update existing user
    public User updateUser(Integer id, User updatedUser) {
        return userRepository.findById(id)
            .map(existingUser -> {
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPassword(updatedUser.getPassword());
                return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Delete user
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}