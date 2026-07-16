package com.example.repository;

import com.example.model.Smoking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmokingRepository extends JpaRepository<Smoking, Long> {

    // 🔍 Find by value (admin-specific, case-insensitive)
    Optional<Smoking> findByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Get all records by admin
    List<Smoking> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Smoking> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<Smoking> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<Smoking> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}