package com.example.repository;

import com.example.model.Family;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    // =========================
    // GET BY ID
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<Family> findByIdAndDeletedAtIsNull(Long id);

    Optional<Family> findByIdAndDeletedAtIsNotNull(Long id);

    // =========================
    // GET ALL
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Family> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY NAME
    // =========================

    Optional<Family> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<Family> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
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
    List<Family> findByIsActiveTrueAndDeletedAtIsNull();

    List<Family> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Family> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Family> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Family> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Family> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
    List<Family> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<Family> findByDeletedAtIsNotNull();
}