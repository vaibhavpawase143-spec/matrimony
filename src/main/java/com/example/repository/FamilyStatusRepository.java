package com.example.repository;

import com.example.model.FamilyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyStatusRepository extends JpaRepository<FamilyStatus, Long> {

    Optional<FamilyStatus> findByName(String name);

    List<FamilyStatus> findByActiveTrue();

    List<FamilyStatus> findByActiveFalse();

    List<FamilyStatus> findByAdminId(Long adminId);

    List<FamilyStatus> findByAdminIdAndActiveTrue(Long adminId);

    List<FamilyStatus> findByNameContainingIgnoreCase(String keyword);
}