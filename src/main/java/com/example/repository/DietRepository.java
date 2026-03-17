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

    // 🔍 Get all active diets
    List<Diet> findByStatusTrue();

    // 🔍 Get all inactive diets
    List<Diet> findByStatusFalse();

    // 🔍 Filter by admin
    List<Diet> findByAdminId(Long adminId);

    // 🔍 Active diets by admin
    List<Diet> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown / autocomplete)
    List<Diet> findByNameContainingIgnoreCase(String keyword);
}