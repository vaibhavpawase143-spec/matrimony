package com.example.repository;

import com.example.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    Optional<State> findByIdAndDeletedAtIsNull(Long id);

    Optional<State> findByIdAndDeletedAtIsNotNull(Long id);

    List<State> findAllByDeletedAtIsNull();

    List<State> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<State> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<State> findByIsActiveTrueAndDeletedAtIsNull();

    List<State> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<State> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<State> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<State> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // COUNTRY
    // =====================================================

    List<State> findByCountry_IdAndAdmin_IdAndDeletedAtIsNull(
            Long countryId,
            Long adminId
    );

    List<State> findByCountry_IdAndAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(
            Long countryId,
            Long adminId
    );

    List<State> findByCountry_IdAndAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(
            Long countryId,
            Long adminId
    );

    // =====================================================
    // SEARCH
    // =====================================================

    List<State> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<State> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
    @Query("""
SELECT s
FROM State s
LEFT JOIN FETCH s.admin
LEFT JOIN FETCH s.country
WHERE s.deletedAt IS NULL
""")
    List<State> findAllWithRelations();
    @Query("""
SELECT s
FROM State s
LEFT JOIN FETCH s.admin
LEFT JOIN FETCH s.country
WHERE s.deletedAt IS NULL
AND s.isActive = true
""")
    List<State> findActiveWithRelations();
    @Query("""
SELECT s
FROM State s
LEFT JOIN FETCH s.admin
LEFT JOIN FETCH s.country
WHERE s.id = :id
AND s.deletedAt IS NULL
""")
    Optional<State> findByIdWithRelations(Long id);
}