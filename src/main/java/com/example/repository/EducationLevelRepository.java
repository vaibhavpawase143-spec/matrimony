package com.example.repository;

import com.example.model.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {

    // =========================
    // Get By ID
    // =========================

    Optional<EducationLevel> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // Get All
    // =========================

    List<EducationLevel> findAllByDeletedAtIsNull();

    // =========================
    // Find By Name
    // =========================

    Optional<EducationLevel> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<EducationLevel> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // Duplicate Check
    // =========================

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // Active / Inactive
    // =========================

    List<EducationLevel> findByIsActiveTrueAndDeletedAtIsNull();

    List<EducationLevel> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // Admin Wise
    // =========================

    List<EducationLevel> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<EducationLevel> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<EducationLevel> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // Search
    // =========================

    List<EducationLevel> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<EducationLevel> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // Soft Deleted Records
    // =========================

    List<EducationLevel> findByDeletedAtIsNotNull();

    Optional<EducationLevel> findByIdAndDeletedAtIsNotNull(Long id);
}