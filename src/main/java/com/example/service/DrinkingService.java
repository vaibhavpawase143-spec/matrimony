package com.example.service;

import com.example.model.Drinking;

import java.util.List;
import java.util.Optional;

public interface DrinkingService {

    // 🔍 Basic CRUD
    Drinking create(Drinking drinking);

    Drinking update(Long id, Drinking drinking);

    void delete(Long id);

    Optional<Drinking> getById(Long id);

    List<Drinking> getAll();

    // 🔍 Find by value
    Optional<Drinking> getByValue(String value);

    Optional<Drinking> getByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<Drinking> getActive();

    List<Drinking> getInactive();

    // 🔍 Admin-based
    List<Drinking> getByAdmin(Long adminId);

    List<Drinking> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Drinking> search(String keyword);

    List<Drinking> searchByAdmin(Long adminId, String keyword);
}