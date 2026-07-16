package com.example.repository;

import com.example.model.Smoking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SmokingRepository extends JpaRepository<Smoking, Long> {

    // =====================================================
    // GET
    // =====================================================

    Optional<Smoking> findByIdAndDeletedAtIsNull(Long id);

    Optional<Smoking> findByIdAndDeletedAtIsNotNull(Long id);

    List<Smoking> findAllByDeletedAtIsNull();

    List<Smoking> findByDeletedAtIsNotNull();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<Smoking> findByIsActiveTrueAndDeletedAtIsNull();

    List<Smoking> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<Smoking> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Smoking> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Smoking> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<Smoking> findByValueContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<Smoking> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );

    Optional<Smoking> findByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );
}