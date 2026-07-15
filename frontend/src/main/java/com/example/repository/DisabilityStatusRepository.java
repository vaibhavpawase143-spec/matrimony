package com.example.repository;

import com.example.model.DisabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisabilityStatusRepository extends JpaRepository<DisabilityStatus, Long> {

    // 🔍 Find by value
    Optional<DisabilityStatus> findByValue(String value);

    // 🔥 Case-insensitive (important)
    Optional<DisabilityStatus> findByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<DisabilityStatus> findByIsActiveTrue();
    List<DisabilityStatus> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<DisabilityStatus> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<DisabilityStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<DisabilityStatus> findByValueContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<DisabilityStatus> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}