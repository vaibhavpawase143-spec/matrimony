package com.example.repository;

import com.example.model.SisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SisterTypeRepository extends JpaRepository<SisterType, Long> {

    // =====================================================
    // BASIC
    // =====================================================

    Optional<SisterType> findByIdAndDeletedAtIsNull(Long id);

    Optional<SisterType> findByIdAndDeletedAtIsNotNull(Long id);

    List<SisterType> findAllByDeletedAtIsNull();

    List<SisterType> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );

    Optional<SisterType> findByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String value,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SisterType> findByIsActiveTrueAndDeletedAtIsNull();

    List<SisterType> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SisterType> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<SisterType> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<SisterType> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SisterType> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<SisterType> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}