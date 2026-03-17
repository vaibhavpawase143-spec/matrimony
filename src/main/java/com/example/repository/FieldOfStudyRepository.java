package com.example.repository;

import com.example.model.FieldOfStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Long> {

    // 🔍 Find by name
    Optional<FieldOfStudy> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<FieldOfStudy> findByStatusTrue();

    // 🔍 Get all inactive records
    List<FieldOfStudy> findByStatusFalse();

    // 🔍 Filter by admin
    List<FieldOfStudy> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<FieldOfStudy> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/autocomplete)
    List<FieldOfStudy> findByNameContainingIgnoreCase(String keyword);
}