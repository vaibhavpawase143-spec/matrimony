package com.example.repository;

import com.example.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {

    // =========================
    // BASIC
    // =========================

    Optional<ProfileType> findByIdAndDeletedAtIsNull(Long id);

    Optional<ProfileType> findByIdAndDeletedAtIsNotNull(Long id);

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

    List<ProfileType> findByIsActiveTrueAndDeletedAtIsNull();

    List<ProfileType> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN
    // =========================

    List<ProfileType> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<ProfileType> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<ProfileType> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<ProfileType> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<ProfileType> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}