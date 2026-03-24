package com.example.repository;

import com.example.model.SubCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<SubCaste> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all by admin
    List<SubCaste> findByAdminId(Long adminId);

    // 🔍 Active / Inactive by admin
    List<SubCaste> findByAdminIdAndIsActiveTrue(Long adminId);

    List<SubCaste> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Filter by caste + admin
    List<SubCaste> findByCaste_IdAndAdminId(Long casteId, Long adminId);

    // 🔍 Active by caste + admin
    List<SubCaste> findByCaste_IdAndAdminIdAndIsActiveTrue(Long casteId, Long adminId);

    // 🔍 Search (admin + keyword)
    List<SubCaste> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}