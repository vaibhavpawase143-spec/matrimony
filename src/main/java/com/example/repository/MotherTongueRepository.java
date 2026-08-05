package com.example.repository;

import com.example.model.MotherTongue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotherTongueRepository extends JpaRepository<MotherTongue, Long> {

    // =========================
    // BASIC
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<MotherTongue> findByIdAndDeletedAtIsNull(Long id);

    Optional<MotherTongue> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findAllByDeletedAtIsNull();

    List<MotherTongue> findByDeletedAtIsNotNull();

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<MotherTongue> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByIsActiveTrueAndDeletedAtIsNull();

    List<MotherTongue> findByIsActiveFalseAndDeletedAtIsNull();
    // =========================
    // ADMIN
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<MotherTongue> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}