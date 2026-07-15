package com.example.repository;

import com.example.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // =========================================
    // FIND
    // =========================================

    Optional<City> findByName(String name);

    Optional<City> findByNameIgnoreCase(String name);

    // =========================================
    // DUPLICATE CHECK
    // =========================================

    boolean existsByName(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndStateIdAndDeletedAtIsNull(
            String name,
            Long stateId
    );

    // =========================================
    // ACTIVE / INACTIVE
    // =========================================

    List<City> findByIsActiveTrue();

    List<City> findByIsActiveFalse();

    List<City> findByIsActiveTrueAndDeletedAtIsNull();

    List<City> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================================
    // STATE
    // =========================================

    List<City> findByState_Id(Long stateId);

    List<City> findByState_IdAndIsActiveTrue(Long stateId);

    List<City> findByState_IdAndDeletedAtIsNull(Long stateId);

    List<City> findByState_IdAndIsActiveTrueAndDeletedAtIsNull(Long stateId);

    Optional<City> findByState_IdAndNameIgnoreCase(
            Long stateId,
            String name
    );

    // =========================================
    // ADMIN
    // =========================================

    List<City> findByAdminId(Long adminId);

    List<City> findByAdminIdAndIsActiveTrue(Long adminId);

    List<City> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<City> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    List<City> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // =========================================
    // SEARCH (KEEP - FRONTEND USES THESE)
    // =========================================

    List<City> findByNameContainingIgnoreCase(String keyword);

    List<City> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<City> findByState_IdAndNameContainingIgnoreCase(
            Long stateId,
            String keyword
    );

    List<City> findByState_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long stateId,
            String keyword
    );

    List<City> findByAdminIdAndNameContainingIgnoreCase(
            Long adminId,
            String keyword
    );

    List<City> findByAdminIdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================================
    // SOFT DELETE
    // =========================================

    List<City> findByDeletedAtIsNull();

    List<City> findByDeletedAtIsNotNull();
}