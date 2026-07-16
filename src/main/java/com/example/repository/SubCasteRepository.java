package com.example.repository;

import com.example.model.SubCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    Optional<SubCaste> findByIdAndDeletedAtIsNull(Long id);

    Optional<SubCaste> findByIdAndDeletedAtIsNotNull(Long id);

    List<SubCaste> findAllByDeletedAtIsNull();

    List<SubCaste> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<SubCaste> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SubCaste> findByIsActiveTrueAndDeletedAtIsNull();

    List<SubCaste> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SubCaste> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<SubCaste> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<SubCaste> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // CASTE
    // =====================================================

    List<SubCaste> findByCaste_IdAndAdmin_IdAndDeletedAtIsNull(
            Long casteId,
            Long adminId
    );

    List<SubCaste> findByCaste_IdAndAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(
            Long casteId,
            Long adminId
    );

    List<SubCaste> findByCaste_IdAndAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(
            Long casteId,
            Long adminId
    );

    // =====================================================
    // SEARCH
    // =====================================================

    List<SubCaste> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<SubCaste> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}