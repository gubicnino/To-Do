package com.example.todo.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
