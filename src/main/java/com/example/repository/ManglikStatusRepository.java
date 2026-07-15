package com.example.repository;

import com.example.model.ManglikStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManglikStatusRepository extends JpaRepository<ManglikStatus, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<ManglikStatus> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<ManglikStatus> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<ManglikStatus> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<ManglikStatus> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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

    List<ManglikStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<ManglikStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<ManglikStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<ManglikStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<ManglikStatus> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<ManglikStatus> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<ManglikStatus> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<ManglikStatus> findByDeletedAtIsNotNull();

    Optional<ManglikStatus> findByIdAndDeletedAtIsNotNull(Long id);
}