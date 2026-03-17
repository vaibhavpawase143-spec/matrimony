package com.example.repository;

import com.example.model.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {

    // 🔍 Find by type (since it's UNIQUE)
    Optional<BloodGroup> findByType(String type);

    // 🔍 Check if type already exists (useful before insert)
    boolean existsByType(String type);

    // 🔍 Get all active blood groups
    List<BloodGroup> findByStatusTrue();

    // 🔍 Get all inactive blood groups
    List<BloodGroup> findByStatusFalse();

    // 🔍 Get by admin id (relation use)
    List<BloodGroup> findByAdminId(Long adminId);

    // 🔍 Get active blood groups by admin
    List<BloodGroup> findByAdminIdAndStatusTrue(Long adminId);
}