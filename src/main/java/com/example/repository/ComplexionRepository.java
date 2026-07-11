package com.example.repository;

import com.example.model.Complexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplexionRepository extends JpaRepository<Complexion, Long> {

    // 🔍 Find by value
    Optional<Complexion> findByValue(String value);

    // 🔥 Case-insensitive (important)
    Optional<Complexion> findByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<Complexion> findByIsActiveTrue();
    List<Complexion> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Complexion> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Complexion> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Complexion> findByValueContainingIgnoreCase(String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<Complexion> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}