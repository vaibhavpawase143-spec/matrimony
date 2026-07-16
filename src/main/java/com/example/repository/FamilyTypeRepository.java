package com.example.repository;

import com.example.model.FamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyTypeRepository extends JpaRepository<FamilyType, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<FamilyType> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<FamilyType> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<FamilyType> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<FamilyType> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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

    List<FamilyType> findByIsActiveTrueAndDeletedAtIsNull();

    List<FamilyType> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyType> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<FamilyType> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<FamilyType> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyType> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<FamilyType> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<FamilyType> findByDeletedAtIsNotNull();

    Optional<FamilyType> findByIdAndDeletedAtIsNotNull(Long id);
}