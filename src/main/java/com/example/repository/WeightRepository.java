package com.example.repository;

import com.example.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

    // 🔍 Find by value (admin-specific, case-insensitive)
    Optional<Weight> findByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Get all weights by admin
    List<Weight> findByAdminId(Long adminId);

    // 🔍 Active weights by admin
    List<Weight> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive weights by admin
    List<Weight> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<Weight> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}