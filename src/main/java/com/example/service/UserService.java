package com.example.service;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // =========================
    // ✅ REGISTER
    // =========================
    User register(UserRegisterRequestDTO request);

    // =========================
    // 🔐 LOGIN
    // =========================
    User login(String email, String password);

    // =========================
    // 🔥 JWT LOGIN
    // =========================
    String loginAndGenerateToken(String email, String password);

    // =========================
    // 🔍 GET BY ID (DTO)
    // =========================
    Optional<UserResponseDTO> getById(Long id);

    // =========================
    // 🔍 GET ALL (DTO)
    // =========================
    List<UserResponseDTO> getAll();

    // =========================
    // 🔍 ACTIVE USERS (DTO)
    // =========================
    List<UserResponseDTO> getActiveUsers();

    // =========================
    // 🔍 SEARCH (ENTITY → Controller maps)
    // =========================
    List<User> search(String keyword);

    // =========================
    // ✅ UPDATE
    // =========================
    User update(Long id, User user);

    // =========================
    // ❌ DEACTIVATE
    // =========================
    void deactivate(Long id);

    // =========================
    // 🔥 PAGINATION (DTO)
    // =========================
    PageResponse<UserResponseDTO> getAllUsers(int page, int size);

    PageResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction);
}