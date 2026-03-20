package com.example.repository;

import com.example.model.BrotherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrotherTypeRepository extends JpaRepository<BrotherType, Long> {

    // 🔍 Find by value
    Optional<BrotherType> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // ✅ Active / Inactive (using isActive)
    List<BrotherType> findByIsActiveTrue();

    List<BrotherType> findByIsActiveFalse();

    // 🔍 Filter by admin (RELATION)
    List<BrotherType> findByAdminId(Long adminId);

    // ✅ Active by admin
    List<BrotherType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search
    List<BrotherType> findByValueContainingIgnoreCase(String keyword);
}