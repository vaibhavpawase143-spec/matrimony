package com.example.repository;

import com.example.model.Smoking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmokingRepository extends JpaRepository<Smoking, Long> {

    // 🔍 Find by value (case-insensitive)
    Optional<Smoking> findByValueIgnoreCase(String value);

    // 🔍 Check duplicate
    boolean existsByValueIgnoreCase(String value);

    // 🔍 Get all active records
    List<Smoking> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<Smoking> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Smoking> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Smoking> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (for dropdown/search)
    List<Smoking> findByValueContainingIgnoreCase(String keyword);
}