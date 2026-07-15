package com.example.repository;

import com.example.model.FamilyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyValueRepository extends JpaRepository<FamilyValue, Long> {

    // 🔍 Find by name
    Optional<FamilyValue> findByName(String name);

    // 🔥 Case-insensitive
    Optional<FamilyValue> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FamilyValue> findByIsActiveTrue();
    List<FamilyValue> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<FamilyValue> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<FamilyValue> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<FamilyValue> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<FamilyValue> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}