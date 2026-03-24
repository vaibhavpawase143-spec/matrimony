package com.example.repository;

import com.example.model.ManglikStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManglikStatusRepository extends JpaRepository<ManglikStatus, Long> {

    // 🔍 Find by name
    Optional<ManglikStatus> findByName(String name);

    // 🔥 Case-insensitive
    Optional<ManglikStatus> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<ManglikStatus> findByIsActiveTrue();
    List<ManglikStatus> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<ManglikStatus> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<ManglikStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<ManglikStatus> findByNameContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<ManglikStatus> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}