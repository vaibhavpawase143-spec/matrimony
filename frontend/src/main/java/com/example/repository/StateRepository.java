package com.example.repository;

import com.example.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    // 🔍 Find by name (admin-specific, case-insensitive)
    Optional<State> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all states by admin
    List<State> findByAdminId(Long adminId);

    // 🔍 Active states by admin
    List<State> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 States by country + admin
    List<State> findByCountry_IdAndAdminId(Long countryId, Long adminId);

    // 🔍 Active states by country + admin
    List<State> findByCountry_IdAndAdminIdAndIsActiveTrue(Long countryId, Long adminId);

    // 🔍 Search (admin + keyword)
    List<State> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}