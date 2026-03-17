package com.example.repository;

import com.example.model.Complexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplexionRepository extends JpaRepository<Complexion, Long> {

    // 🔍 Find by value (e.g., Fair, Wheatish, Dark)
    Optional<Complexion> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<Complexion> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Complexion> findByStatusFalse();

    // 🔍 Filter by admin
    List<Complexion> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Complexion> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (useful for dropdown/search)
    List<Complexion> findByValueContainingIgnoreCase(String keyword);
}