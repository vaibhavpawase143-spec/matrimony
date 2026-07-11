package com.example.service;

import com.example.model.MaritalStatus;

import java.util.List;
import java.util.Optional;

public interface MaritalStatusService {

    // 🔍 Basic CRUD
    MaritalStatus create(MaritalStatus maritalStatus);

    MaritalStatus update(Long id, MaritalStatus maritalStatus);

    void delete(Long id);

    Optional<MaritalStatus> getById(Long id);

    List<MaritalStatus> getAll();

    // 🔍 Find by name
    Optional<MaritalStatus> getByName(String name);

    Optional<MaritalStatus> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<MaritalStatus> getActive();

    List<MaritalStatus> getInactive();

    // 🔍 Admin-based
    List<MaritalStatus> getByAdmin(Long adminId);

    List<MaritalStatus> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<MaritalStatus> search(String keyword);

    List<MaritalStatus> searchByAdmin(Long adminId, String keyword);
}