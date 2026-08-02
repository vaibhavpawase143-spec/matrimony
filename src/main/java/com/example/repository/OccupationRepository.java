package com.example.repository;

import com.example.model.Occupation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    // =========================
    // BASIC
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<Occupation> findByIdAndDeletedAtIsNull(Long id);

    Optional<Occupation> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findAllByDeletedAtIsNull();

    List<Occupation> findByDeletedAtIsNotNull();

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<Occupation> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByIsActiveTrueAndDeletedAtIsNull();

    List<Occupation> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);
    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<Occupation> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}