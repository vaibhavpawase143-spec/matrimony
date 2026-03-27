package com.example.service;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // =========================
    // ✅ REGISTER (INTERFACE MATCH)
    // =========================
    User register(User user);

    // ✅ FIXED (DTO)
    User register(UserRegisterRequestDTO request);

    // 🔐 Login
    User login(String email, String password);

    // 🔥 JWT Login
    String loginAndGenerateToken(String email, String password);

    // 🔍 Get by ID
    Optional<User> getById(Long id);

    // 🔍 Get by email
    Optional<User> getByEmail(String email);

    // 🔍 Get all users
    List<User> getAll();

    // 🔍 Active users
    List<User> getActiveUsers();

    // 🔍 Search
    List<User> search(String keyword);

    // ✅ Update
    User update(Long id, User user);

    // ❌ Soft delete
    void deactivate(Long id);

}