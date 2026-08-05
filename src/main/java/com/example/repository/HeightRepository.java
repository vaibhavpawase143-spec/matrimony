package com.example.repository;

import com.example.model.Height;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeightRepository extends JpaRepository<Height, Long> {

    // =========================
    // GET BY ID
    // =========================

    Optional<Height> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // GET ALL
    // =========================

    List<Height> findAllByDeletedAtIsNull();

    // =========================
    // FIND BY HEIGHT
    // =========================

    Optional<Height> findByHeightIgnoreCaseAndDeletedAtIsNull(String height);

    Optional<Height> findByHeightIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String height,
            Long adminId
    );

    // =========================
    // DUPLICATE CHECK
    // =========================

    boolean existsByHeightIgnoreCaseAndDeletedAtIsNull(String height);

    boolean existsByHeightIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String height,
            Long adminId
    );

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<Height> findByIsActiveTrueAndDeletedAtIsNull();

    List<Height> findByIsActiveFalseAndDeletedAtIsNull();

    // =========================
    // ADMIN WISE
    // =========================

    List<Height> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Height> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Height> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<Height> findByHeightContainingIgnoreCaseAndDeletedAtIsNull(String keyword);

    List<Height> findByAdmin_IdAndHeightContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =========================
    // SOFT DELETED
    // =========================

    List<Height> findByDeletedAtIsNotNull();

    Optional<Height> findByIdAndDeletedAtIsNotNull(Long id);
    @Query("""
SELECT h
FROM Height h
LEFT JOIN FETCH h.admin
WHERE h.deletedAt IS NULL
""")
    List<Height> findAllWithAdmin();
    @Query("""
SELECT h
FROM Height h
LEFT JOIN FETCH h.admin
WHERE h.isActive = true
AND h.deletedAt IS NULL
""")
    List<Height> findActiveWithAdmin();
    @Query("""
SELECT h
FROM Height h
LEFT JOIN FETCH h.admin
WHERE h.admin.id = :adminId
AND h.deletedAt IS NULL
""")
    List<Height> findByAdminWithAdmin(Long adminId);
    @Query("""
SELECT h
FROM Height h
LEFT JOIN FETCH h.admin
WHERE h.admin.id = :adminId
AND h.isActive = true
AND h.deletedAt IS NULL
""")
    List<Height> findActiveByAdminWithAdmin(Long adminId);
}