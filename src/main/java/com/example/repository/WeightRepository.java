package com.example.repository;

import com.example.model.Weight;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    Optional<Weight> findByIdAndDeletedAtIsNull(Long id);

    Optional<Weight> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
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

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByIsActiveTrueAndDeletedAtIsNull();

    List<Weight> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<Weight> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}