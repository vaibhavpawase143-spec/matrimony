package com.example.repository;

import com.example.model.Complexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplexionRepository extends JpaRepository<Complexion, Long> {

    // 🔍 Find by name
    Optional<Complexion> findByName(String name);

    // 🔥 Case-insensitive (important)
    Optional<Complexion> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Complexion> findByIsActiveTrue();
    List<Complexion> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Complexion> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Complexion> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Complexion> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<Complexion> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}