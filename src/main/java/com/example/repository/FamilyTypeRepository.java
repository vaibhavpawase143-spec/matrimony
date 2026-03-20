package com.example.repository;

import com.example.model.FamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyTypeRepository extends JpaRepository<FamilyType, Long> {

    // 🔍 Find by name (case-insensitive preferred)
    Optional<FamilyType> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // ✅ STANDARD FIELD (isActive)
    List<FamilyType> findByIsActiveTrue();

    List<FamilyType> findByIsActiveFalse();

    // 🔍 Filter by admin (RELATION SAFE)
    List<FamilyType> findByAdminId(Long adminId);

    // ✅ Active records by admin
    List<FamilyType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<FamilyType> findByNameContainingIgnoreCase(String keyword);
}