package com.example.service;

import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(User user);

    Optional<User> login(String email, String password);

    Optional<User> getById(Long id);

    List<User> getAll();

    List<User> getAllActive();

    User update(Long id, User user);

    void deactivate(Long id);

    List<User> search(String keyword);

    // ✅ Missing method (add this)
    boolean existsByEmail(String email);
}