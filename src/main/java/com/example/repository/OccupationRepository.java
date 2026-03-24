package com.example.repository;

import com.example.model.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<Occupation> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<Occupation> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Occupation> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<Occupation> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<Occupation> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}