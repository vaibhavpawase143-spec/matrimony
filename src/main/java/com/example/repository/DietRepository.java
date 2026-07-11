package com.example.repository;

import com.example.model.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    // 🔍 Find by name
    Optional<Diet> findByName(String name);

    // 🔥 Case-insensitive (important)
    Optional<Diet> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Diet> findByIsActiveTrue();
    List<Diet> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Diet> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Diet> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Diet> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<Diet> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}