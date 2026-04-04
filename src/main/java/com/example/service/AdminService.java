package com.example.service;

import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;

import java.util.List;

public interface AdminService {

    // ================= REGISTER =================
    Admin register(Admin admin);

    Admin create(Admin admin);

    // 🔥 RETURN DTO
    AdminResponseDTO getById(Long id);

    List<AdminResponseDTO> getAll();

    Admin update(Long id, Admin admin);

    void delete(Long id);

    Admin login(String username, String password);

    Admin findByEmail(String email);
}