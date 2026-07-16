package com.example.service;

import com.example.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface FieldOfStudyService {

    // 🔍 Basic CRUD
    FieldOfStudy create(FieldOfStudy fieldOfStudy);

    FieldOfStudy update(Long id, FieldOfStudy fieldOfStudy);

    void delete(Long id);

    Optional<FieldOfStudy> getById(Long id);

    List<FieldOfStudy> getAll();

    // 🔍 Find by name
    Optional<FieldOfStudy> getByName(String name);

    Optional<FieldOfStudy> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FieldOfStudy> getActive();

    List<FieldOfStudy> getInactive();

    // 🔍 Admin-based
    List<FieldOfStudy> getByAdmin(Long adminId);

    List<FieldOfStudy> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<FieldOfStudy> search(String keyword);

    List<FieldOfStudy> searchByAdmin(Long adminId, String keyword);
}