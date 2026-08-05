package com.example.repository;

import com.example.model.Income;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // =========================
    // GET BY ID
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<Income> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Income> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY RANGE
    // =========================

    Optional<Income> findByRangeIgnoreCaseAndDeletedAtIsNull(String range);

    Optional<Income> findByRangeIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String range,
            Long adminId
    );

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByRangeIgnoreCaseAndDeletedAtIsNull(String range);

    boolean existsByRangeIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String range,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Income> findByIsActiveTrueAndDeletedAtIsNull();

    List<Income> findByIsActiveFalseAndDeletedAtIsNull();
    // =========================
    // ADMIN WISE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Income> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Income> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Income> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Income> findByRangeContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
    List<Income> findByAdmin_IdAndRangeContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // DELETED
    // =========================

    List<Income> findByDeletedAtIsNotNull();

    Optional<Income> findByIdAndDeletedAtIsNotNull(Long id);
}