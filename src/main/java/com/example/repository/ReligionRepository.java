package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    Optional<Religion> findByIdAndDeletedAtIsNull(Long id);

    Optional<Religion> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
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

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByIsActiveTrueAndDeletedAtIsNull();

    List<Religion> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);
    // =====================================================
    // SEARCH
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<Religion> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}