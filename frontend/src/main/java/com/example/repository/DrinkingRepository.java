package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkingRepository extends JpaRepository<Drinking, Long> {

    // 🔍 Find by value
    Optional<Drinking> findByValue(String value);

    // 🔥 Case-insensitive
    Optional<Drinking> findByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<Drinking> findByIsActiveTrue();
    List<Drinking> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<Drinking> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Drinking> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (global)
    List<Drinking> findByValueContainingIgnoreCase(String keyword);

    // 🔥 Search within admin
    List<Drinking> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}