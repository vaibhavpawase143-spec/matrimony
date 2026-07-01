package com.example.service;

import com.example.dto.request.AdminFilterDTO;
import com.example.dto.request.AdminResetPasswordDTO;
import com.example.dto.request.AdminUpdateDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.dto.response.AdminStatsDTO;
import com.example.model.Admin;
import org.springframework.data.domain.Page;

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
    // ================= ADMIN MANAGEMENT V2 =================

    Page<AdminResponseDTO> getAllAdmins(
            AdminFilterDTO filter
    );

    AdminResponseDTO updateAdmin(
            Long id,
            AdminUpdateDTO dto
    );

    void activateAdmin(
            Long id
    );

    void deactivateAdmin(
            Long id
    );

    void resetPassword(
            Long id,
            AdminResetPasswordDTO dto
    );

    AdminStatsDTO getStatistics();
}