package com.example.repository;

import com.example.model.MotherTongue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotherTongueRepository extends JpaRepository<MotherTongue, Long> {

    // 🔍 Find by name (Hindi, Marathi, English)
    Optional<MotherTongue> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<MotherTongue> findByStatusTrue();

    // 🔍 Get all inactive records
    List<MotherTongue> findByStatusFalse();

    // 🔍 Filter by admin
    List<MotherTongue> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<MotherTongue> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<MotherTongue> findByNameContainingIgnoreCase(String keyword);
}