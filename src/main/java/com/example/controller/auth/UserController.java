package com.example.controller.auth; // auth folder

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.login(email, password);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.status(401).build());
    }

    // Get user by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        Optional<User> user = userService.getById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/user/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User updated) {
        return ResponseEntity.ok(userService.update(id, updated));
    }

    // Deactivate user
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // Search users (admin feature)
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.search(keyword));
    }

    // Get all active users
    @GetMapping("/users/active")
    public ResponseEntity<List<User>> getAllActive() {
        return ResponseEntity.ok(userService.getAllActive());
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}