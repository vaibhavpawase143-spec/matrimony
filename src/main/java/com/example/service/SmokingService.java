package com.example.service;

import com.example.model.Smoking;

import java.util.List;
import java.util.Optional;

public interface SmokingService {

    // ✅ Create / Save
    Smoking save(Smoking smoking);

    // ✅ Get by ID
    Optional<Smoking> getById(Long id);

    // 🔍 Get all
    List<Smoking> getAll();

    // 🔍 Get by admin
    List<Smoking> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<Smoking> getActiveByAdmin(Long adminId);

    List<Smoking> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<Smoking> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by value (admin-specific)
    Optional<Smoking> getByValueAndAdmin(String value, Long adminId);

    // ✅ Delete
    void delete(Long id);
}