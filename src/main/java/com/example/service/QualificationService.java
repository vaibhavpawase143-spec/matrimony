package com.example.service;

import com.example.model.Qualification;

import java.util.List;
import java.util.Optional;

public interface QualificationService {

    // ✅ Create / Save
    Qualification save(Qualification qualification);

    // ✅ Get by ID
    Optional<Qualification> getById(Long id);

    // 🔍 Get all
    List<Qualification> getAll();

    // 🔍 Get by admin
    List<Qualification> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<Qualification> getActiveByAdmin(Long adminId);

    List<Qualification> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<Qualification> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name (admin-specific)
    Optional<Qualification> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}