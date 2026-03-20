package com.example.repository;

import com.example.model.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

    // 🔍 Find by name (case-insensitive)
    Optional<MaritalStatus> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // ✅ Get all active
    List<MaritalStatus> findByIsActiveTrue();

    // ❌ Get all inactive
    List<MaritalStatus> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<MaritalStatus> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<MaritalStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<MaritalStatus> findByNameContainingIgnoreCase(String keyword);
}