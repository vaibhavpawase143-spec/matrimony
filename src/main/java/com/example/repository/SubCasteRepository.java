package com.example.repository;

import com.example.model.SubCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {

    // 🔍 Find by name
    Optional<SubCaste> findByNameIgnoreCase(String name);

    // 🔍 Check duplicate
    boolean existsByNameIgnoreCase(String name);

    // ✅ Active / Inactive
    List<SubCaste> findByIsActiveTrue();

    List<SubCaste> findByIsActiveFalse();

    // 🔍 Filter by Admin (RELATION)
    List<SubCaste> findByAdminId(Long adminId);

    List<SubCaste> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Filter by Caste (RELATION)
    List<SubCaste> findByCaste_Id(Long casteId);

    List<SubCaste> findByCaste_IdAndIsActiveTrue(Long casteId);

    // 🔍 Search
    List<SubCaste> findByNameContainingIgnoreCase(String keyword);

    // 🔍 Optional direct field (only if exists in entity)
    List<SubCaste> findByCasteId(Long casteId);
}