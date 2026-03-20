package com.example.repository;

import com.example.model.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Long> {

    // 🔍 Find by name (case-insensitive recommended)
    Optional<FieldOfStudy> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // ✅ STANDARD FIELD (isActive)
    List<FieldOfStudy> findByIsActiveTrue();

    List<FieldOfStudy> findByIsActiveFalse();

    // 🔍 Filter by admin (RELATION SAFE)
    List<FieldOfStudy> findByAdminId(Long adminId);

    // ✅ Active records by admin
    List<FieldOfStudy> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<FieldOfStudy> findByNameContainingIgnoreCase(String keyword);
}