package com.example.repository;

import com.example.model.SisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SisterTypeRepository extends JpaRepository<SisterType, Long> {

    // 🔍 Find by value (admin-specific)
    Optional<SisterType> findByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Get all records by admin
    List<SisterType> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<SisterType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<SisterType> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<SisterType> findByAdminIdAndValueContainingIgnoreCase(Long adminId, String keyword);
}