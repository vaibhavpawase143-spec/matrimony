package com.example.repository;

import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);

    boolean existsByName(String name);

    List<Country> findByIsActiveTrue();

    List<Country> findByIsActiveFalse();

    List<Country> findByAdminId(Long adminId);

    List<Country> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Country> findByNameContainingIgnoreCase(String keyword);
}