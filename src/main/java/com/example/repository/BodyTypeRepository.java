package com.example.repository;

import com.example.model.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {

    // ================= Duplicate Check =================

    boolean existsByValueIgnoreCaseAndDeletedAtIsNull(String value);

    Optional<BodyType> findByValueIgnoreCase(String value);

    // ================= Basic Queries =================

    List<BodyType> findByIsActiveTrue();

    List<BodyType> findByIsActiveFalse();

    // ================= Admin Queries =================

    List<BodyType> findByAdminId(Long adminId);

    List<BodyType> findByAdminIdAndIsActiveTrue(Long adminId);

    List<BodyType> findByAdminIdAndIsActiveFalse(Long adminId);

    // ================= Soft Delete =================

    List<BodyType> findByDeletedAtIsNull();

    List<BodyType> findByDeletedAtIsNotNull();

    List<BodyType> findByIsActiveTrueAndDeletedAtIsNull();

    List<BodyType> findByIsActiveFalseAndDeletedAtIsNull();

    List<BodyType> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<BodyType> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    List<BodyType> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<BodyType> findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);
}