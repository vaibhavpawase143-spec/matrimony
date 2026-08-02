package com.example.repository;

import com.example.model.Smoking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmokingRepository extends JpaRepository<Smoking, Long> {

    // =====================================================
    // GET
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    Optional<Smoking> findByIdAndDeletedAtIsNull(Long id);

    Optional<Smoking> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<Smoking> findAllByDeletedAtIsNull();

    List<Smoking> findByDeletedAtIsNotNull();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Smoking> findByIsActiveTrueAndDeletedAtIsNull();

    List<Smoking> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Smoking> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<Smoking> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Smoking> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<Smoking> findByValueContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    @EntityGraph(attributePaths = "admin")
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