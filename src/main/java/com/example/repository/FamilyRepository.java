package com.example.repository;

import com.example.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    // 🔍 Find by name
    Optional<Family> findByName(String name);

    // 🔥 Case-insensitive
    Optional<Family> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Family> findByIsActiveTrue();
    List<Family> findByIsActiveFalse();

    // 🔍 Admin-based
    List<Family> findByAdminId(Long adminId);
    List<Family> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<Family> findByNameContainingIgnoreCase(String keyword);
    List<Family> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}