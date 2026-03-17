package com.example.repository;

import com.example.model.FamilyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyValueRepository extends JpaRepository<FamilyValue, Long> {

    // 🔍 Find by name
    Optional<FamilyValue> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<FamilyValue> findByStatusTrue();

    // 🔍 Get all inactive records
    List<FamilyValue> findByStatusFalse();

    // 🔍 Filter by admin
    List<FamilyValue> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<FamilyValue> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/search)
    List<FamilyValue> findByNameContainingIgnoreCase(String keyword);
}