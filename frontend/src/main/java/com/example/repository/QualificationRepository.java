package com.example.repository;

import com.example.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<Qualification> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<Qualification> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Qualification> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<Qualification> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<Qualification> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}