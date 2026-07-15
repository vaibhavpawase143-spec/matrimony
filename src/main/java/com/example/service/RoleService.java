package com.example.service;

import com.example.dto.request.RoleRequestDTO;
import com.example.dto.response.RoleResponseDTO;
import com.example.model.Role;

import java.util.List;

public interface RoleService {

    // =====================================================
    // CRUD
    // =====================================================

    RoleResponseDTO create(RoleRequestDTO requestDto);

    RoleResponseDTO update(Long id, RoleRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    RoleResponseDTO getById(Long id);

    List<RoleResponseDTO> getAll();

    List<RoleResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<RoleResponseDTO> getActive();

    List<RoleResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<RoleResponseDTO> getByAdmin(Long adminId);

    List<RoleResponseDTO> getActiveByAdmin(Long adminId);

    List<RoleResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<RoleResponseDTO> search(String keyword);

    List<RoleResponseDTO> searchByAdmin(Long adminId, String keyword);

    // =====================================================
    // JWT / SPRING SECURITY
    // =====================================================

    Role getRoleEntityByName(String name);
}