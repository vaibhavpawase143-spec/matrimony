package com.example.repository;

import com.example.model.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

    // 🔍 Find by name (Single, Married, Divorced)
    Optional<MaritalStatus> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<MaritalStatus> findByStatusTrue();

    // 🔍 Get all inactive records
    List<MaritalStatus> findByStatusFalse();

    // 🔍 Filter by admin
    List<MaritalStatus> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<MaritalStatus> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<MaritalStatus> findByNameContainingIgnoreCase(String keyword);
}