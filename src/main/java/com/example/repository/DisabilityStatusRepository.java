package com.example.repository;

import com.example.model.DisabilityStatus;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByDeletedAtIsNull();

    List<DisabilityStatus> findByDeletedAtIsNotNull();

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByIsActiveTrueAndDeletedAtIsNull();

    List<DisabilityStatus> findByIsActiveFalseAndDeletedAtIsNull();

    // ==========================
    // ADMIN
    // ==========================

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<DisabilityStatus> findByAdmin_IdAndDeletedAtIsNotNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByValueContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<DisabilityStatus> findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}