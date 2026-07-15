package com.example.repository;

import com.example.model.Employed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployedRepository extends JpaRepository<Employed, Long> {

    // 🔍 Find by name
    Optional<Employed> findByName(String name);

    // 🔥 Case-insensitive
    Optional<Employed> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<Employed> findByIsActiveTrue();
    List<Employed> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Employed> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Employed> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Employed> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<Employed> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}