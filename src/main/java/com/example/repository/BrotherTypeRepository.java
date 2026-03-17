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

    // 🔍 Check duplicate before insert
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<BrotherType> findByStatusTrue();

    // 🔍 Get all inactive records
    List<BrotherType> findByStatusFalse();

    // 🔍 Filter by admin
    List<BrotherType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<BrotherType> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (useful for dropdown/filter)
    List<BrotherType> findByValueContainingIgnoreCase(String keyword);
}