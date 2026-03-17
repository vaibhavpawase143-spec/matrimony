package com.example.repository;

import com.example.model.DisabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisabilityStatusRepository extends JpaRepository<DisabilityStatus, Long> {

    // 🔍 Find by value (Yes, No, Physically Challenged, etc.)
    Optional<DisabilityStatus> findByValue(String value);

    // 🔍 Check duplicate
    boolean existsByValue(String value);

    // 🔍 Get all active records
    List<DisabilityStatus> findByStatusTrue();

    // 🔍 Get all inactive records
    List<DisabilityStatus> findByStatusFalse();

    // 🔍 Filter by admin
    List<DisabilityStatus> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<DisabilityStatus> findByAdminIdAndStatusTrue(Long adminId);

    // 🔍 Search (useful for dropdown/search)
    List<DisabilityStatus> findByValueContainingIgnoreCase(String keyword);
}