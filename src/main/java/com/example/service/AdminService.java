package com.example.service;

import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;

import java.util.List;

public interface AdminService {

    Admin register(Admin admin);

    String blockUser(Long userId);

    // ✅ FIXED
    String unblockUser(Long userId);

    Admin create(Admin admin);

    AdminResponseDTO getById(Long id);

    List<AdminResponseDTO> getAll();

    Admin update(Long id, Admin admin);

    void delete(Long id);

    Admin login(String username, String password);

    Admin findByEmail(String email);
}