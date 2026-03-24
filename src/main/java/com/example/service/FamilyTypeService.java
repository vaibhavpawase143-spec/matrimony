package com.example.service;

import com.example.model.FamilyType;

import java.util.List;
import java.util.Optional;

public interface FamilyTypeService {

    // 🔍 Basic CRUD
    FamilyType create(FamilyType familyType);

    FamilyType update(Long id, FamilyType familyType);

    void delete(Long id);

    Optional<FamilyType> getById(Long id);

    List<FamilyType> getAll();

    // 🔍 Find by name
    Optional<FamilyType> getByName(String name);

    Optional<FamilyType> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FamilyType> getActive();

    List<FamilyType> getInactive();

    // 🔍 Admin-based
    List<FamilyType> getByAdmin(Long adminId);

    List<FamilyType> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<FamilyType> search(String keyword);

    List<FamilyType> searchByAdmin(Long adminId, String keyword);
}