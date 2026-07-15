package com.example.repository;

import com.example.model.MotherTongue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotherTongueRepository extends JpaRepository<MotherTongue, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<MotherTongue> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<MotherTongue> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<MotherTongue> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<MotherTongue> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<MotherTongue> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}