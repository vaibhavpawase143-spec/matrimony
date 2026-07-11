package com.example.repository;

import com.example.model.FamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyTypeRepository extends JpaRepository<FamilyType, Long> {

    // 🔍 Find by name
    Optional<FamilyType> findByName(String name);

    // 🔥 Case-insensitive
    Optional<FamilyType> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<FamilyType> findByIsActiveTrue();
    List<FamilyType> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<FamilyType> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<FamilyType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<FamilyType> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<FamilyType> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}