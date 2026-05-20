package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkingRepository extends JpaRepository<Drinking, Long> {

    // 🔍 Find by name
    Optional<Drinking> findByName(String name);

    // 🔥 Case-insensitive
    Optional<Drinking> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Drinking> findByIsActiveTrue();
    List<Drinking> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Drinking> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Drinking> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Drinking> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<Drinking> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}