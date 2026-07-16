package com.example.repository;

import com.example.model.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<FieldOfStudy> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<FieldOfStudy> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<FieldOfStudy> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<FieldOfStudy> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FieldOfStudy> findByIsActiveTrueAndDeletedAtIsNull();

    List<FieldOfStudy> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<FieldOfStudy> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<FieldOfStudy> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<FieldOfStudy> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FieldOfStudy> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<FieldOfStudy> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // DELETED
    // =========================

    List<FieldOfStudy> findByDeletedAtIsNotNull();

    Optional<FieldOfStudy> findByIdAndDeletedAtIsNotNull(Long id);
}