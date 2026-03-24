package com.example.service;

import com.example.model.Occupation;

import java.util.List;
import java.util.Optional;

public interface OccupationService {

    // 🔍 Basic CRUD
    Occupation create(Occupation occupation);

    Occupation update(Long id, Occupation occupation);

    void delete(Long id);

    Optional<Occupation> getById(Long id);

    // 🔍 Admin-based
    List<Occupation> getByAdmin(Long adminId);

    List<Occupation> getActiveByAdmin(Long adminId);

    List<Occupation> getInactiveByAdmin(Long adminId);

    // 🔍 Find by name (admin-specific)
    Optional<Occupation> getByNameAndAdmin(String name, Long adminId);

    // ✅ Duplicate check
    boolean existsByNameAndAdmin(String name, Long adminId);

    // 🔍 Search
    List<Occupation> searchByAdmin(Long adminId, String keyword);
}