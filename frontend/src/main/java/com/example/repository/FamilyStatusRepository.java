package com.example.repository;

import com.example.model.FamilyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyStatusRepository extends JpaRepository<FamilyStatus, Long> {

    // 🔍 Find by name
    Optional<FamilyStatus> findByName(String name);

    // 🔥 Case-insensitive
    Optional<FamilyStatus> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive (FIXED)
    List<FamilyStatus> findByIsActiveTrue();
    List<FamilyStatus> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<FamilyStatus> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<FamilyStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<FamilyStatus> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<FamilyStatus> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}