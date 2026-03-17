package com.example.repository;

import com.example.model.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    // 🔍 Find by name (Engineer, Doctor, Teacher)
    Optional<Occupation> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<Occupation> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Occupation> findByStatusFalse();

    // 🔍 Filter by admin
    List<Occupation> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Occupation> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/autocomplete)
    List<Occupation> findByNameContainingIgnoreCase(String keyword);
}