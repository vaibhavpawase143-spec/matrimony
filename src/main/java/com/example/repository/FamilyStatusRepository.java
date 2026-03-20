package com.example.repository;

import com.example.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyStatusRepository extends JpaRepository<Family, Long> {

    // 🔍 Find by name (case-insensitive)
    Optional<Family> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate (case-insensitive)
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Get all active records
    List<Family> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<Family> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Family> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Family> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<Family> findByNameContainingIgnoreCase(String keyword);
}