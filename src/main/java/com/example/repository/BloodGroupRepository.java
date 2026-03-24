package com.example.repository;

import com.example.model.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {

    // 🔍 Find by type (case-sensitive by default)
    Optional<BloodGroup> findByType(String type);

    // 🔍 Case-insensitive (better for real-world usage)
    Optional<BloodGroup> findByTypeIgnoreCase(String type);

    // ✅ Duplicate check
    boolean existsByType(String type);

    boolean existsByTypeIgnoreCase(String type);

    // 🔍 Active / Inactive
    List<BloodGroup> findByIsActiveTrue();
    List<BloodGroup> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<BloodGroup> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<BloodGroup> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔥 Optional (best practice combo filter)
    List<BloodGroup> findByAdminIdAndTypeIgnoreCase(Long adminId, String type);
}