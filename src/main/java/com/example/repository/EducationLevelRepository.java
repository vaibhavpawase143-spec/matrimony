package com.example.repository;

import com.example.model.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {

    // 🔍 Find by name
    Optional<EducationLevel> findByName(String name);

    // 🔥 Case-insensitive
    Optional<EducationLevel> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<EducationLevel> findByIsActiveTrue();
    List<EducationLevel> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<EducationLevel> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<EducationLevel> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<EducationLevel> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<EducationLevel> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}