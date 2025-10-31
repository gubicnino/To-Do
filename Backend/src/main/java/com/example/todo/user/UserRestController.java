package com.example.todo.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users - Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return (List<User>) userService.getAllUsers();
    }

    // GET /api/v1/users/{id} - Get single user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // POST /api/v1/users - Create new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT /api/v1/users/{id} - Update existing user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE /api/v1/users/{id} - Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
