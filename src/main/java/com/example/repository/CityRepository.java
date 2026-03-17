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

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active cities
    List<City> findByStatusTrue();

    // 🔍 Get cities by state (IMPORTANT)
    List<City> findByStateId(Long stateId);

    // 🔍 Active cities by state (used in dropdown)
    List<City> findByStateIdAndStatusTrue(Long stateId);

    // 🔍 Filter by admin
    List<City> findByAdminId(Long adminId);

    // 🔍 Search city (for autocomplete)
    List<City> findByNameContainingIgnoreCase(String keyword);

    // 🔍 Search within state
    List<City> findByStateIdAndNameContainingIgnoreCase(Long stateId, String keyword);
}