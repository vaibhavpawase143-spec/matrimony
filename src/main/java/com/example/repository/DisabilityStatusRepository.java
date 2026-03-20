package com.example.repository;

import com.example.model.DisabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisabilityStatusRepository extends JpaRepository<DisabilityStatus, Long> {

    // 🔍 Find by exact value
    Optional<DisabilityStatus> findByValue(String value);

    // 🔍 Check duplicate value
    boolean existsByValue(String value);

    // ✅ Get all active records
    List<DisabilityStatus> findByIsActiveTrue();

    // ❌ Get all inactive records
    List<DisabilityStatus> findByIsActiveFalse();

    // 🔍 Filter by Admin ID
    List<DisabilityStatus> findByAdminId(Long adminId);

    // ✅ Active records by Admin
    List<DisabilityStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search by keyword (case-insensitive)
    List<DisabilityStatus> findByValueContainingIgnoreCase(String keyword);
}