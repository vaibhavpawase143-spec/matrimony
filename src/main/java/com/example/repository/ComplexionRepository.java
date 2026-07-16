package com.example.repository;

import com.example.model.Complexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplexionRepository extends JpaRepository<Complexion, Long> {

    // =========================================
    // FIND
    // =========================================

    Optional<Complexion> findByValue(String value);

    Optional<Complexion> findByValueIgnoreCase(String value);

    // =========================================
    // DUPLICATE CHECK
    // =========================================

    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    boolean existsByValueIgnoreCaseAndDeletedAtIsNull(String value);

    // =========================================
    // ACTIVE / INACTIVE
    // =========================================

    List<Complexion> findByIsActiveTrue();

    List<Complexion> findByIsActiveFalse();

    List<Complexion> findByIsActiveTrueAndDeletedAtIsNull();

    List<Complexion> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================================
    // ADMIN
    // =========================================

    List<Complexion> findByAdminId(Long adminId);

    List<Complexion> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Complexion> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<Complexion> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    List<Complexion> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // =========================================
    // SEARCH (KEEP - FRONTEND USES THESE)
    // =========================================

    List<Complexion> findByValueContainingIgnoreCase(String keyword);

    List<Complexion> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Complexion> findByAdminIdAndValueContainingIgnoreCase(
            Long adminId,
            String keyword
    );

    List<Complexion> findByAdminIdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================================
    // SOFT DELETE
    // =========================================

    List<Complexion> findByDeletedAtIsNull();

    List<Complexion> findByDeletedAtIsNotNull();
}