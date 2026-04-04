package com.example.mapper;

import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;

public class AdminMapper {

    private AdminMapper() {}

    public static AdminResponseDTO toDTO(Admin admin) {

        if (admin == null) return null;

        AdminResponseDTO dto = new AdminResponseDTO();

        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setIsActive(admin.getIsActive());

        // 🔥 ROLE FIX
        if (admin.getRole() != null) {
            dto.setRole(admin.getRole().getName());
        }

        dto.setLastLogin(admin.getLastLogin());
        dto.setCreatedAt(admin.getCreatedAt());

        return dto;
    }
}