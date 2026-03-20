package com.example.repository;

import com.example.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeightRepository extends JpaRepository<Height, Long> {

    // 🔍 Find by height (exact match, case-insensitive)
    Optional<Height> findByHeightIgnoreCase(String height);

    // 🔍 Get all active records
    List<Height> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<Height> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Height> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Height> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<Height> findByHeightContainingIgnoreCase(String keyword);
}