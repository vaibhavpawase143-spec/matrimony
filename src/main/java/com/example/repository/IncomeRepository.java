package com.example.repository;

import com.example.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // 🔍 Find by range (exact match, case-insensitive)
    Optional<Income> findByRangeIgnoreCase(String range);

    // 🔍 Search by range (partial match)
    List<Income> findByRangeContainingIgnoreCase(String keyword);

    // ✅ STANDARD FIELD (isActive)
    List<Income> findByIsActiveTrue();

    List<Income> findByIsActiveFalse();

    // 🔍 Filter by admin (RELATION SAFE)
    List<Income> findByAdminId(Long adminId);

    // ✅ Active records by admin
    List<Income> findByAdminIdAndIsActiveTrue(Long adminId);
}