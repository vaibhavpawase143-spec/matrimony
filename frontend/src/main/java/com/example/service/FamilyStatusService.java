package com.example.service;

import com.example.model.FamilyStatus;

import java.util.List;
import java.util.Optional;

public interface FamilyStatusService {

    // 🔍 Basic CRUD
    FamilyStatus create(FamilyStatus familyStatus);

    FamilyStatus update(Long id, FamilyStatus familyStatus);

    void delete(Long id);

    Optional<FamilyStatus> getById(Long id);

    List<FamilyStatus> getAll();

    // 🔍 Find by name
    Optional<FamilyStatus> getByName(String name);

    Optional<FamilyStatus> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FamilyStatus> getActive();

    List<FamilyStatus> getInactive();

    // 🔍 Admin-based
    List<FamilyStatus> getByAdmin(Long adminId);

    List<FamilyStatus> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<FamilyStatus> search(String keyword);

    List<FamilyStatus> searchByAdmin(Long adminId, String keyword);
}