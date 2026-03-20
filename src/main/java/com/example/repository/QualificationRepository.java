package com.example.repository;

import com.example.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

    // 🔍 Find by name (B.Tech, MBA, M.Sc)
    Optional<Qualification> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // 🔍 Get all active records
    List<Qualification> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<Qualification> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Qualification> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Qualification> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<Qualification> findByNameContainingIgnoreCase(String keyword);
}