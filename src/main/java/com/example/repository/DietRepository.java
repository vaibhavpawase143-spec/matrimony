package com.example.repository;

import com.example.model.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    // 🔍 Find by name (Veg, Non-Veg, Vegan)
    Optional<Diet> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // ✅ STANDARD FIELD (isActive)
    List<Diet> findByIsActiveTrue();

    List<Diet> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Diet> findByAdminId(Long adminId);

    // ✅ Active diets by admin
    List<Diet> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<Diet> findByNameContainingIgnoreCase(String keyword);
}