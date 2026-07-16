package com.example.repository;

import com.example.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<Income> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

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

    List<Income> findByIsActiveTrueAndDeletedAtIsNull();

    List<Income> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<Income> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Income> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Income> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<Income> findByRangeContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

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