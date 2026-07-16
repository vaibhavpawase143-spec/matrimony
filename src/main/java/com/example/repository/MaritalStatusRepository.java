package com.example.repository;

import com.example.model.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

    // =========================
    // BASIC
    // =========================

    Optional<MaritalStatus> findByIdAndDeletedAtIsNull(Long id);

    Optional<MaritalStatus> findByIdAndDeletedAtIsNotNull(Long id);

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

    List<MaritalStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<MaritalStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN
    // =========================

    List<MaritalStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<MaritalStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<MaritalStatus> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<MaritalStatus> findByNameContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<MaritalStatus> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}