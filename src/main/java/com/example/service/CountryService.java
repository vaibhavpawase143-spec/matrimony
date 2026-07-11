package com.example.service;

import com.example.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    // 🔍 Basic CRUD
    Country create(Country country);

    Country update(Long id, Country country);

    void delete(Long id);

    Optional<Country> getById(Long id);

    List<Country> getAll();

    // 🔍 Find by name
    Optional<Country> getByName(String name);

    Optional<Country> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Country> getActive();

    List<Country> getInactive();

    // 🔍 Admin-based
    List<Country> getByAdmin(Long adminId);

    List<Country> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Country> search(String keyword);

    List<Country> searchByAdmin(Long adminId, String keyword);
}