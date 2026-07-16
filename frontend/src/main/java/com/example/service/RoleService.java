package com.example.service;

import com.example.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    // =========================
    // ✅ CREATE / SAVE
    // =========================
    Role save(Role role);

    // =========================
    // 🔍 GET BY ID
    // =========================
    Optional<Role> getById(Long id);

    // =========================
    // 🔍 GET ALL (GLOBAL)
    // =========================
    List<Role> getAll();

    // =========================
    // 🔍 GLOBAL ROLE (IMPORTANT FOR JWT)
    // =========================
    Optional<Role> getByName(String name);

    // =========================
    // 🔍 ADMIN BASED (OPTIONAL)
    // =========================
    List<Role> getByAdmin(Long adminId);

    List<Role> getActiveByAdmin(Long adminId);

    List<Role> getInactiveByAdmin(Long adminId);

    List<Role> searchByAdmin(Long adminId, String keyword);

    Optional<Role> getByNameAndAdmin(String name, Long adminId);

    // =========================
    // ❌ DELETE
    // =========================
    void delete(Long id);
}