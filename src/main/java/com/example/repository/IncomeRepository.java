package com.example.repository;

import com.example.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // 🔍 Find by range
    Optional<Income> findByRange(String range);

    // 🔥 Case-insensitive
    Optional<Income> findByRangeIgnoreCase(String range);

    // ✅ Duplicate check
    boolean existsByRange(String range);

    boolean existsByRangeIgnoreCase(String range);

    // 🔍 Active / Inactive
    List<Income> findByIsActiveTrue();
    List<Income> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Income> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Income> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Income> findByRangeContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<Income> findByAdminIdAndRangeContainingIgnoreCase(Long adminId, String keyword);
}