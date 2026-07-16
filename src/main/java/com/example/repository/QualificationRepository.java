package com.example.repository;

import com.example.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

    // =========================
    // BASIC
    // =========================

    Optional<Qualification> findByIdAndDeletedAtIsNull(Long id);

    Optional<Qualification> findByIdAndDeletedAtIsNotNull(Long id);

    List<Qualification> findAllByDeletedAtIsNull();

    List<Qualification> findByDeletedAtIsNotNull();

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<Qualification> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<Qualification> findByIsActiveTrueAndDeletedAtIsNull();

    List<Qualification> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN
    // =========================

    List<Qualification> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Qualification> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Qualification> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<Qualification> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Qualification> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}