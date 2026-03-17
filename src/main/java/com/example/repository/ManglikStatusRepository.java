package com.example.repository;

import com.example.model.ManglikStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManglikStatusRepository extends JpaRepository<ManglikStatus, Long> {

    // 🔍 Find by name (Yes, No, Partial)
    Optional<ManglikStatus> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Filter by admin
    List<ManglikStatus> findByAdminId(Long adminId);

    // 🔍 Search (for dropdown/filter)
    List<ManglikStatus> findByNameContainingIgnoreCase(String keyword);
}