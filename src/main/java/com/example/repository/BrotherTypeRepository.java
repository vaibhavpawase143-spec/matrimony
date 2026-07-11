package com.example.repository;

import com.example.model.BrotherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrotherTypeRepository extends JpaRepository<BrotherType, Long> {

    // 🔍 Find by value (optional use)
    Optional<BrotherType> findByValueIgnoreCase(String value);

    // 🔥 Duplicate check (ADMIN SAFE)
    boolean existsByValueIgnoreCaseAndAdminId(String value, Long adminId);

    // 🔍 Active / Inactive
    List<BrotherType> findByIsActiveTrue();
    List<BrotherType> findByIsActiveFalse();

    // 🔍 Admin-based filtering
    List<BrotherType> findByAdminId(Long adminId);

    List<BrotherType> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔒 Secure fetch
    Optional<BrotherType> findByIdAndAdminId(Long id, Long adminId);

    // 🔍 Search (admin scoped)
    List<BrotherType> findByValueContainingIgnoreCaseAndAdminId(String keyword, Long adminId);
}