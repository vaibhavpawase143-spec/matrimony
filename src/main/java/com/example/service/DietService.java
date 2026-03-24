package com.example.service;

import com.example.model.Diet;

import java.util.List;
import java.util.Optional;

public interface DietService {

    // 🔍 Basic CRUD
    Diet create(Diet diet);

    Diet update(Long id, Diet diet);

    void delete(Long id);

    Optional<Diet> getById(Long id);

    List<Diet> getAll();

    // 🔍 Find by name
    Optional<Diet> getByName(String name);

    Optional<Diet> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Diet> getActive();

    List<Diet> getInactive();

    // 🔍 Admin-based
    List<Diet> getByAdmin(Long adminId);

    List<Diet> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Diet> search(String keyword);

    List<Diet> searchByAdmin(Long adminId, String keyword);
}