package com.example.repository;

import com.example.model.FamilyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyStatusRepository extends JpaRepository<FamilyStatus, Long> {

    // 🔍 Find by name
    Optional<FamilyStatus> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<FamilyStatus> findByStatusTrue();

    // 🔍 Get all inactive records
    List<FamilyStatus> findByStatusFalse();

    // 🔍 Filter by admin
    List<FamilyStatus> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<FamilyStatus> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<FamilyStatus> findByNameContainingIgnoreCase(String keyword);
}