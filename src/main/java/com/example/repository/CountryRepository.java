package com.example.repository;

import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // 🔍 Find by name (unique)
    Optional<Country> findByName(String name);

    // 🔍 Check duplicate before insert
    boolean existsByName(String name);

    // 🔍 Get all active countries
    List<Country> findByStatusTrue();

    // 🔍 Get all inactive countries
    List<Country> findByStatusFalse();

    // 🔍 Filter by admin
    List<Country> findByAdminId(Long adminId);

    // 🔍 Active countries by admin
    List<Country> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search country (for dropdown/search)
    List<Country> findByNameContainingIgnoreCase(String keyword);
}