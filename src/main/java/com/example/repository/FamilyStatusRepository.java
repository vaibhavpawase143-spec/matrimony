package com.example.repository;

import com.example.model.FamilyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyStatusRepository extends JpaRepository<FamilyStatus, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<FamilyStatus> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<FamilyStatus> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<FamilyStatus> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<FamilyStatus> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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

    List<FamilyStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<FamilyStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<FamilyStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<FamilyStatus> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyStatus> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<FamilyStatus> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<FamilyStatus> findByDeletedAtIsNotNull();

    Optional<FamilyStatus> findByIdAndDeletedAtIsNotNull(Long id);
}