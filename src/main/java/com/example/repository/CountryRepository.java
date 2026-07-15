package com.example.repository;

import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // =========================================
    // FIND
    // =========================================

    Optional<Country> findByName(String name);

    Optional<Country> findByNameIgnoreCase(String name);

    // =========================================
    // DUPLICATE CHECK
    // =========================================

    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    // =========================================
    // ACTIVE / INACTIVE
    // =========================================

    List<Country> findByIsActiveTrue();

    List<Country> findByIsActiveFalse();

    List<Country> findByIsActiveTrueAndDeletedAtIsNull();

    List<Country> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================================
    // ADMIN
    // =========================================

    List<Country> findByAdminId(Long adminId);

    List<Country> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Country> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<Country> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    List<Country> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // =========================================
    // SEARCH (KEEP - FRONTEND USES THESE)
    // =========================================

    List<Country> findByNameContainingIgnoreCase(String keyword);

    List<Country> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Country> findByAdminIdAndNameContainingIgnoreCase(
            Long adminId,
            String keyword
    );

    List<Country> findByAdminIdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================================
    // SOFT DELETE
    // =========================================

    List<Country> findByDeletedAtIsNull();

    List<Country> findByDeletedAtIsNotNull();
}