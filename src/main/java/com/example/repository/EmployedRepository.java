package com.example.repository;

import com.example.model.Employed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployedRepository extends JpaRepository<Employed, Long> {

    // 🔍 Find by name
    Optional<Employed> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get all active records
    List<Employed> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Employed> findByStatusFalse();

    // 🔍 Filter by admin
    List<Employed> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Employed> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/search)
    List<Employed> findByNameContainingIgnoreCase(String keyword);
}