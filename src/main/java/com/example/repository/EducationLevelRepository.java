package com.example.repository;

import com.example.model.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {

    // 🔍 Find by name
    Optional<EducationLevel> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Get all active records
    List<EducationLevel> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<EducationLevel> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<EducationLevel> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<EducationLevel> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<EducationLevel> findByNameContainingIgnoreCase(String keyword);
}