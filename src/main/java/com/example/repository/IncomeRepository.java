package com.example.repository;

import com.example.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // 🔍 Find by range (e.g., 0-5 LPA, 5-10 LPA)
    Optional<Income> findByRange(String range);

    // 🔍 Check duplicate
    boolean existsByRange(String range);

    // 🔍 Filter by admin
    List<Income> findByAdminId(Long adminId);

    // 🔍 Search (for dropdown/filter)
    List<Income> findByRangeContainingIgnoreCase(String keyword);
}