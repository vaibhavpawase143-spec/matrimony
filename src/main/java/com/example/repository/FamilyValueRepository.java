package com.example.repository;

import com.example.model.FamilyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyValueRepository extends JpaRepository<FamilyValue, Long> {

    // 🔍 Find by name (case-insensitive)
    Optional<FamilyValue> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Get all active records
    List<FamilyValue> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<FamilyValue> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<FamilyValue> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<FamilyValue> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (for dropdown/search)
    List<FamilyValue> findByNameContainingIgnoreCase(String keyword);
}