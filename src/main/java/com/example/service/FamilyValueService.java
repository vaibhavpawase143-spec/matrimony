package com.example.service;

import com.example.model.FamilyValue;

import java.util.List;
import java.util.Optional;

public interface FamilyValueService {

    // 🔍 Basic CRUD
    FamilyValue create(FamilyValue familyValue);

    FamilyValue update(Long id, FamilyValue familyValue);

    void delete(Long id);

    Optional<FamilyValue> getById(Long id);

    List<FamilyValue> getAll();

    // 🔍 Find by name
    Optional<FamilyValue> getByName(String name);

    Optional<FamilyValue> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FamilyValue> getActive();

    List<FamilyValue> getInactive();

    // 🔍 Admin-based
    List<FamilyValue> getByAdmin(Long adminId);

    List<FamilyValue> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<FamilyValue> search(String keyword);

    List<FamilyValue> searchByAdmin(Long adminId, String keyword);
}