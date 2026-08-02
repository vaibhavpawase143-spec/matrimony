package com.example.repository;

import com.example.model.MaritalStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

    // =========================
    // BASIC
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<MaritalStatus> findByIdAndDeletedAtIsNull(Long id);

    Optional<MaritalStatus> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findAllByDeletedAtIsNull();

    List<MaritalStatus> findByDeletedAtIsNotNull();

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<MaritalStatus> findByIsActiveFalseAndDeletedAtIsNull();
    // =========================
    // ADMIN
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
    List<MaritalStatus> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}