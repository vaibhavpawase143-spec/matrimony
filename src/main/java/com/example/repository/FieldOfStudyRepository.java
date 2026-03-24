package com.example.repository;

import com.example.model.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Long> {

    // 🔍 Find by name
    Optional<FieldOfStudy> findByName(String name);

    // 🔥 Case-insensitive
    Optional<FieldOfStudy> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FieldOfStudy> findByIsActiveTrue();
    List<FieldOfStudy> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<FieldOfStudy> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<FieldOfStudy> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<FieldOfStudy> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<FieldOfStudy> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}