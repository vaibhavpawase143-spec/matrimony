package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<Religion> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<Religion> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Religion> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<Religion> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<Religion> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}