package com.example.service;

import com.example.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    // ✅ Create / Save
    Role save(Role role);

    // ✅ Get by ID
    Optional<Role> getById(Long id);

    // 🔍 Get all
    List<Role> getAll();

    // 🔍 Get by admin
    List<Role> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<Role> getActiveByAdmin(Long adminId);

    List<Role> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<Role> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name (admin-specific)
    Optional<Role> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}