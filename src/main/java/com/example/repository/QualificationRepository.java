package com.example.repository;

import com.example.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

    // 🔍 Find by name (B.Tech, MBA, M.Sc)
    Optional<Qualification> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<Qualification> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Qualification> findByStatusFalse();

    // 🔍 Filter by admin
    List<Qualification> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Qualification> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search
    List<Qualification> findByNameContainingIgnoreCase(String keyword);
}