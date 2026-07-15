package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    Optional<Religion> findByIdAndDeletedAtIsNull(Long id);

    Optional<Religion> findByIdAndDeletedAtIsNotNull(Long id);

    List<Religion> findAllByDeletedAtIsNull();

    List<Religion> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<Religion> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<Religion> findByIsActiveTrueAndDeletedAtIsNull();

    List<Religion> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<Religion> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Religion> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Religion> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<Religion> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Religion> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}
