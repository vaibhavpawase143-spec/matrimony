package com.example.repository;

import com.example.model.Drinking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkingRepository extends JpaRepository<Drinking, Long> {

    // 🔍 Find by value (Never, Occasionally, Regularly)
    Optional<Drinking> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<Drinking> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Drinking> findByStatusFalse();

    // 🔍 Filter by admin
    List<Drinking> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Drinking> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/filter)
    List<Drinking> findByValueContainingIgnoreCase(String keyword);
}