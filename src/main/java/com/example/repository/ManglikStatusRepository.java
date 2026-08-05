package com.example.repository;

import com.example.model.ManglikStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManglikStatusRepository extends JpaRepository<ManglikStatus, Long> {

    // =========================
    // GET BY ID
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<ManglikStatus> findByIdAndDeletedAtIsNull(Long id);

    Optional<ManglikStatus> findByIdAndDeletedAtIsNotNull(Long id);

    // =========================
    // GET ALL
    // =========================

    @EntityGraph(attributePaths = "admin")
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

    @EntityGraph(attributePaths = "admin")
    List<ManglikStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<ManglikStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<ManglikStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<ManglikStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<ManglikStatus> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<ManglikStatus> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
    List<ManglikStatus> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<ManglikStatus> findByDeletedAtIsNotNull();


}