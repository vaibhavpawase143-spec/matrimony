package com.example.service;

import com.example.model.Religion;

import java.util.List;
import java.util.Optional;

public interface ReligionService {

    // ✅ Create / Save
    Religion save(Religion religion);

    // ✅ Get by ID
    Optional<Religion> getById(Long id);

    // 🔍 Get all
    List<Religion> getAll();

    // 🔍 Get by admin
    List<Religion> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<Religion> getActiveByAdmin(Long adminId);

    List<Religion> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<Religion> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name (admin-specific)
    Optional<Religion> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}