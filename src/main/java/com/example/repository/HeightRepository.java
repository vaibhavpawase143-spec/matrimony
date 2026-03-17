package com.example.repository;

import com.example.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeightRepository extends JpaRepository<Height, Long> {

    // 🔍 Find by height (e.g., 5.5 ft, 170 cm)
    Optional<Height> findByHeight(String height);

    // 🔍 Check duplicate
    boolean existsByHeight(String height);

    // 🔍 Get all active records
    List<Height> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Height> findByStatusFalse();

    // 🔍 Filter by admin
    List<Height> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Height> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<Height> findByHeightContainingIgnoreCase(String keyword);
}