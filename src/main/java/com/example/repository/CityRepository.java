package com.example.repository;

import com.example.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    boolean existsByName(String name);

    // ✅ Active cities
    List<City> findByIsActiveTrue();

    // ✅ By state (RELATION SAFE)
    List<City> findByState_Id(Long stateId);

    List<City> findByState_IdAndIsActiveTrue(Long stateId);

    // ✅ By admin
    List<City> findByAdminId(Long adminId);

    // ✅ Search
    List<City> findByNameContainingIgnoreCase(String keyword);

    List<City> findByState_IdAndNameContainingIgnoreCase(Long stateId, String keyword);
}