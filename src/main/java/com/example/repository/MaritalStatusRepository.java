package com.example.repository;

import com.example.model.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

    // 🔍 Find by name
    Optional<MaritalStatus> findByName(String name);

    // 🔥 Case-insensitive
    Optional<MaritalStatus> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<MaritalStatus> findByIsActiveTrue();
    List<MaritalStatus> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<MaritalStatus> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<MaritalStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<MaritalStatus> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<MaritalStatus> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}