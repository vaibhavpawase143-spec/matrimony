package com.example.repository;

import com.example.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    Optional<Weight> findByIdAndDeletedAtIsNull(Long id);

    Optional<Weight> findByIdAndDeletedAtIsNotNull(Long id);

    List<Weight> findAllByDeletedAtIsNull();

    List<Weight> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );

    Optional<Weight> findByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<Weight> findByIsActiveTrueAndDeletedAtIsNull();

    List<Weight> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<Weight> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Weight> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Weight> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<Weight> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Weight> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}