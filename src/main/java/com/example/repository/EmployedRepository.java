package com.example.repository;

import com.example.model.Employed;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployedRepository extends JpaRepository<Employed, Long> {

    // =========================
    // Get By ID
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<Employed> findByIdAndDeletedAtIsNull(Long id);

    Optional<Employed> findByIdAndDeletedAtIsNotNull(Long id);

    // =========================
    // Get All
    // =========================

    @EntityGraph(attributePaths = "admin")
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

    @EntityGraph(attributePaths = "admin")
    List<Employed> findByIsActiveTrueAndDeletedAtIsNull();

    List<Employed> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // Admin Wise
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Employed> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Employed> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Employed> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // Search
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Employed> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
    List<Employed> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // Soft Deleted Records
    // =========================

    List<Employed> findByDeletedAtIsNotNull();
}