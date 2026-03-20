package com.example.repository;

import com.example.model.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {

    // 🔍 Find by type
    Optional<BloodGroup> findByType(String type);

    // 🔍 Check duplicate
    boolean existsByType(String type);

    // ✅ Use STANDARD FIELD (isActive)
    List<BloodGroup> findByIsActiveTrue();

    List<BloodGroup> findByIsActiveFalse();

    // 🔍 Get by admin
    List<BloodGroup> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<BloodGroup> findByAdminIdAndIsActiveTrue(Long adminId);
}