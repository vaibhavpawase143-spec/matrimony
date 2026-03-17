package com.example.repository;

import com.example.model.SisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SisterTypeRepository extends JpaRepository<SisterType, Long> {

    // 🔍 Find by value (No Sister, 1 Sister, etc.)
    Optional<SisterType> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<SisterType> findByStatusTrue();

    // 🔍 Get all inactive records
    List<SisterType> findByStatusFalse();

    // 🔍 Filter by admin
    List<SisterType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<SisterType> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (for dropdown/filter)
    List<SisterType> findByValueContainingIgnoreCase(String keyword);
}