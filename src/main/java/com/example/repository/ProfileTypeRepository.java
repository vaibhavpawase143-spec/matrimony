package com.example.repository;

import com.example.model.ProfileType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {

    // =========================
    // BASIC
    // =========================

    @EntityGraph(attributePaths = "admin")
    Optional<ProfileType> findByIdAndDeletedAtIsNull(Long id);

    Optional<ProfileType> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findAllByDeletedAtIsNull();

    List<ProfileType> findByDeletedAtIsNotNull();

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<ProfileType> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findByIsActiveTrueAndDeletedAtIsNull();

    List<ProfileType> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<ProfileType> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<ProfileType> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}