package com.example.repository;

import com.example.model.Employed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployedRepository extends JpaRepository<Employed, Long> {

    Optional<Employed> findByName(String name);

    boolean existsByName(String name);

    // ✅ STANDARD FIELD (isActive)
    List<Employed> findByIsActiveTrue();

    List<Employed> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Employed> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Employed> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<Employed> findByNameContainingIgnoreCase(String keyword);
}