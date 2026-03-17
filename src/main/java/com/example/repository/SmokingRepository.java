package com.example.repository;

import com.example.model.Smoking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmokingRepository extends JpaRepository<Smoking, Long> {

    // 🔍 Find by value (Never, Occasionally, Regularly)
    Optional<Smoking> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<Smoking> findByStatusTrue();

    // 🔍 Get all inactive records
    List<Smoking> findByStatusFalse();

    // 🔍 Filter by admin
    List<Smoking> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<Smoking> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<Smoking> findByValueContainingIgnoreCase(String keyword);
}