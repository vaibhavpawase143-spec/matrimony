package com.example.repository;

import com.example.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    // 🔍 Find by name
    Optional<State> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active states
    List<State> findByStatusTrue();

    // 🔍 Get states by country (VERY IMPORTANT)
    List<State> findByCountryId(Long countryId);

    // 🔍 Active states by country (dropdown use)
    List<State> findByCountryIdAndStatusTrue(Long countryId);

    // 🔍 Filter by admin
    List<State> findByAdminId(Long adminId);

    // 🔍 Search
    List<State> findByNameContainingIgnoreCase(String keyword);
}