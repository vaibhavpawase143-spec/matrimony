package com.example.repository;

import com.example.model.FamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyTypeRepository extends JpaRepository<FamilyType, Long> {

    // 🔍 Find by name
    Optional<FamilyType> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<FamilyType> findByStatusTrue();

    // 🔍 Get all inactive records
    List<FamilyType> findByStatusFalse();

    // 🔍 Filter by admin
    List<FamilyType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<FamilyType> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/search)
    List<FamilyType> findByNameContainingIgnoreCase(String keyword);
}