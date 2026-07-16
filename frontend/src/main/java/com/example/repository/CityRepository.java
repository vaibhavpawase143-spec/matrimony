package com.example.repository;

import com.example.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // 🔍 Find by name
    Optional<City> findByName(String name);

    // 🔥 Case-insensitive (important)
    Optional<City> findByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<City> findByIsActiveTrue();
    List<City> findByIsActiveFalse();

    // 🔍 State-based filtering (RELATION SAFE)
    List<City> findByState_Id(Long stateId);

    List<City> findByState_IdAndIsActiveTrue(Long stateId);

    // 🔥 State + Name (important for validation)
    Optional<City> findByState_IdAndNameIgnoreCase(Long stateId, String name);

    // 🔍 Admin-based filtering
    List<City> findByAdminId(Long adminId);

    // 🔥 Admin + Active
    List<City> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<City> findByNameContainingIgnoreCase(String keyword);

    // 🔍 Search within state
    List<City> findByState_IdAndNameContainingIgnoreCase(Long stateId, String keyword);

    // 🔥 Search within admin (multi-tenant support)
    List<City> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}