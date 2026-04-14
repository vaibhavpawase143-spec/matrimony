package com.example.repository;

import com.example.model.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    List<Occupation> findByAdminId(Long adminId);

    List<Occupation> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Occupation> findByAdminIdAndIsActiveFalse(Long adminId);

    Optional<Occupation> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    List<Occupation> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}