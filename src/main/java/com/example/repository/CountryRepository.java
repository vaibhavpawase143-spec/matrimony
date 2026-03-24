package com.example.repository;

import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // 🔍 Find by name
    Optional<Country> findByName(String name);

    // 🔥 Case-insensitive (important)
    Optional<Country> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Country> findByIsActiveTrue();
    List<Country> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Country> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Country> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Country> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<Country> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}