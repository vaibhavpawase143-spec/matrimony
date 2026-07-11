package com.example.repository;

import com.example.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeightRepository extends JpaRepository<Height, Long> {

    // 🔍 Find by height
    Optional<Height> findByHeight(String height);

    // 🔥 Case-insensitive
    Optional<Height> findByHeightIgnoreCase(String height);

    // ✅ Duplicate check
    boolean existsByHeight(String height);

    boolean existsByHeightIgnoreCase(String height);

    // 🔍 Active / Inactive
    List<Height> findByIsActiveTrue();
    List<Height> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Height> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Height> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Height> findByHeightContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<Height> findByAdminIdAndHeightContainingIgnoreCase(Long adminId, String keyword);
}