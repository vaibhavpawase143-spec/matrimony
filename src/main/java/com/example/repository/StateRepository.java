package com.example.repository;

import com.example.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findByName(String name);

    boolean existsByName(String name);

    List<State> findByIsActiveTrue();

    List<State> findByCountry_Id(Long countryId);

    List<State> findByCountry_IdAndIsActiveTrue(Long countryId);

    List<State> findByAdminId(Long adminId);

    List<State> findByNameContainingIgnoreCase(String keyword);
}