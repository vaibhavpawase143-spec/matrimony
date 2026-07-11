package com.example.service;

import com.example.model.SisterType;

import java.util.List;
import java.util.Optional;

public interface SisterTypeService {

    // ✅ Create / Save
    SisterType save(SisterType sisterType);

    // ✅ Get by ID
    Optional<SisterType> getById(Long id);

    // 🔍 Get all
    List<SisterType> getAll();

    // 🔍 Get by admin
    List<SisterType> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<SisterType> getActiveByAdmin(Long adminId);

    List<SisterType> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<SisterType> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by value (admin-specific)
    Optional<SisterType> getByValueAndAdmin(String value, Long adminId);

    // ✅ Delete
    void delete(Long id);
}