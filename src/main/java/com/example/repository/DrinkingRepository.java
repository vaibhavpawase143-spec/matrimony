package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkingRepository extends JpaRepository<Drinking, Long> {

    // 🔍 Find by value (Never, Occasionally, Regularly)
    Optional<Drinking> findByValueIgnoreCase(String value);
    List<Drinking> findByNameContainingIgnoreCase(String name);
    // 🔍 Check duplicate
    boolean existsByValueIgnoreCase(String value);

    // ✅ Active records
    List<Drinking> findByIsActiveTrue();

    List<Drinking> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Drinking> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<Drinking> findByAdminIdAndIsActiveTrue(Long adminId);

    // ✅ FIXED SEARCH
    List<Drinking> findByValueContainingIgnoreCase(String keyword);
}