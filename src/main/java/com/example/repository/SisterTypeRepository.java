package com.example.repository;

import com.example.model.SisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SisterTypeRepository extends JpaRepository<SisterType, Long> {

    // 🔍 Find by value
    Optional<SisterType> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<SisterType> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<SisterType> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<SisterType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<SisterType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (useful for dropdown/search)
    List<SisterType> findByValueContainingIgnoreCase(String keyword);
}