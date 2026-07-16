package com.example.repository;

import com.example.model.DisabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisabilityStatusRepository extends JpaRepository<DisabilityStatus, Long> {

    // ==========================
    // DUPLICATE CHECK
    // ==========================

    boolean existsByValueIgnoreCaseAndDeletedAtIsNull(String value);

    // ==========================
    // GET
    // ==========================

    List<DisabilityStatus> findByDeletedAtIsNull();

    List<DisabilityStatus> findByDeletedAtIsNotNull();

    List<DisabilityStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<DisabilityStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // ==========================
    // ADMIN
    // ==========================

    List<DisabilityStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<DisabilityStatus> findByAdmin_IdAndDeletedAtIsNotNull(Long adminId);

    List<DisabilityStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    List<DisabilityStatus> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<DisabilityStatus> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}