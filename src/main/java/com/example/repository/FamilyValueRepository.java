package com.example.repository;

import com.example.model.FamilyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyValueRepository extends JpaRepository<FamilyValue, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<FamilyValue> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<FamilyValue> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<FamilyValue> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<FamilyValue> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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

    List<FamilyValue> findByIsActiveTrueAndDeletedAtIsNull();

    List<FamilyValue> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyValue> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<FamilyValue> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<FamilyValue> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyValue> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<FamilyValue> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<FamilyValue> findByDeletedAtIsNotNull();

    Optional<FamilyValue> findByIdAndDeletedAtIsNotNull(Long id);
}