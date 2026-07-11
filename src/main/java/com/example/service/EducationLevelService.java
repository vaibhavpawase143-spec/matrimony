package com.example.service;

import com.example.model.EducationLevel;

import java.util.List;
import java.util.Optional;

public interface EducationLevelService {

    // 🔍 Basic CRUD
    EducationLevel create(EducationLevel educationLevel);

    EducationLevel update(Long id, EducationLevel educationLevel);

    void delete(Long id);

    Optional<EducationLevel> getById(Long id);

    List<EducationLevel> getAll();

    // 🔍 Find by name
    Optional<EducationLevel> getByName(String name);

    Optional<EducationLevel> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<EducationLevel> getActive();

    List<EducationLevel> getInactive();

    // 🔍 Admin-based
    List<EducationLevel> getByAdmin(Long adminId);

    List<EducationLevel> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<EducationLevel> search(String keyword);

    List<EducationLevel> searchByAdmin(Long adminId, String keyword);
}