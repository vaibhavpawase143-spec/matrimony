package com.example.repository;

import com.example.model.BrotherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrotherTypeRepository extends JpaRepository<BrotherType, Long> {

    // ================= Duplicate Check =================

    Optional<BrotherType> findByValueIgnoreCase(String value);

    boolean existsByValueIgnoreCaseAndDeletedAtIsNull(String value);

    // ================= Active / Inactive =================

    List<BrotherType> findByIsActiveTrue();

    List<BrotherType> findByIsActiveFalse();

    // ================= Admin =================

    List<BrotherType> findByAdminId(Long adminId);

    List<BrotherType> findByAdminIdAndIsActiveTrue(Long adminId);

    // ================= Soft Delete =================

    List<BrotherType> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<BrotherType> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<BrotherType> findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    List<BrotherType> findByAdminIdAndDeletedAtIsNotNull(Long adminId);
}