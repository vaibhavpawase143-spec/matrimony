package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    // 🔍 Find by name
    Optional<Religion> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active religions
    List<Religion> findByStatusTrue();

    // 🔍 Get all inactive religions
    List<Religion> findByStatusFalse();

    // 🔍 Filter by admin
    List<Religion> findByAdminId(Long adminId);

    // 🔍 Active religions by admin
    List<Religion> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/filter)
    List<Religion> findByNameContainingIgnoreCase(String keyword);
}