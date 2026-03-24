package com.example.service;

import com.example.model.Employed;

import java.util.List;
import java.util.Optional;

public interface EmployedService {

    // 🔍 Basic CRUD
    Employed create(Employed employed);

    Employed update(Long id, Employed employed);

    void delete(Long id);

    Optional<Employed> getById(Long id);

    List<Employed> getAll();

    // 🔍 Find by name
    Optional<Employed> getByName(String name);

    Optional<Employed> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Employed> getActive();

    List<Employed> getInactive();

    // 🔍 Admin-based
    List<Employed> getByAdmin(Long adminId);

    List<Employed> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Employed> search(String keyword);

    List<Employed> searchByAdmin(Long adminId, String keyword);
}