package com.example.repository;

import com.example.model.ManglikStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManglikStatusRepository extends JpaRepository<ManglikStatus, Long> {

    // 🔍 Find by name (Yes, No, Partial)
    Optional<ManglikStatus> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Filter by admin
    List<ManglikStatus> findByAdminId(Long adminId);

    // 🔍 Get all active records
    List<ManglikStatus> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<ManglikStatus> findByIsActiveFalse();

    // 🔍 Active records by admin
    List<ManglikStatus> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<ManglikStatus> findByNameContainingIgnoreCase(String keyword);
}