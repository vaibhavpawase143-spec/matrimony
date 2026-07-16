package com.example.repository;

import com.example.model.Employed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployedRepository extends JpaRepository<Employed, Long> {

    // =========================
    // Get By ID
    // =========================

    Optional<Employed> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // Get All
    // =========================

    List<Employed> findAllByDeletedAtIsNull();

    // =========================
    // Find By Name
    // =========================

    Optional<Employed> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<Employed> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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

    List<Employed> findByIsActiveTrueAndDeletedAtIsNull();

    List<Employed> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // Admin Wise
    // =========================

    List<Employed> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Employed> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Employed> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // Search
    // =========================

    List<Employed> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<Employed> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // Soft Deleted Records
    // =========================

    List<Employed> findByDeletedAtIsNotNull();

    Optional<Employed> findByIdAndDeletedAtIsNotNull(Long id);
}