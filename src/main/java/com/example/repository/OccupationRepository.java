package com.example.repository;

import com.example.model.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    List<Occupation> findByIsActiveTrue();

    List<Occupation> findByIsActiveFalse();

    List<Occupation> findByAdminId(Long adminId);

    List<Occupation> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Occupation> findByNameContainingIgnoreCase(String keyword);
}