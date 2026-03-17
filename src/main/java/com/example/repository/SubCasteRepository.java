package com.example.repository;

import com.example.model.SubCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {

    // 🔍 Find by name
    Optional<SubCaste> findByName(String name);

    // 🔍 Check duplicate (improve later with casteId)
    boolean existsByName(String name);

    // 🔍 Get all active sub-castes
    List<SubCaste> findByStatusTrue();

    // 🔍 Get by caste (VERY IMPORTANT)
    List<SubCaste> findByCasteId(Long casteId);

    // 🔍 Active sub-castes by caste (dropdown)
    List<SubCaste> findByCasteIdAndStatusTrue(Long casteId);

    // 🔍 Filter by admin
    List<SubCaste> findByAdminId(Long adminId);

    // 🔍 Search
    List<SubCaste> findByNameContainingIgnoreCase(String keyword);
}