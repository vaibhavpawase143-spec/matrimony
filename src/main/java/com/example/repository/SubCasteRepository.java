package com.example.repository;

import com.example.model.SubCaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCasteRepository extends JpaRepository<SubCaste, Long> {

    // =========================================
    // FIND BY NAME
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE LOWER(s.name) = LOWER(:name)
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
    """)
    Optional<SubCaste> findAccessibleByName(
            String name,
            Long adminId
    );

    // =========================================
    // DUPLICATE CHECK
    // =========================================

    @Query("""
        SELECT COUNT(s) > 0
        FROM SubCaste s
        WHERE LOWER(s.name) = LOWER(:name)
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
    """)
    boolean existsAccessible(
            String name,
            Long adminId
    );

    // =========================================
    // GET BY ID
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE s.id = :id
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
    """)
    Optional<SubCaste> findAccessibleById(
            Long id,
            Long adminId
    );

    // =========================================
    // GET ALL
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> findAllAvailable(Long adminId);

    // =========================================
    // ACTIVE
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE s.isActive = true
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> findAllActiveAvailable(Long adminId);

    // =========================================
    // INACTIVE
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE s.isActive = false
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> findAllInactiveAvailable(Long adminId);

    // =========================================
    // CASTE FILTER
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE s.caste.id = :casteId
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> findAvailableByCaste(
            Long casteId,
            Long adminId
    );

    // =========================================
    // ACTIVE BY CASTE
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE s.caste.id = :casteId
        AND s.isActive = true
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> findActiveAvailableByCaste(
            Long casteId,
            Long adminId
    );

    // =========================================
    // SEARCH
    // =========================================

    @Query("""
        SELECT s FROM SubCaste s
        WHERE LOWER(s.name)
        LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND (
            s.admin.id = :adminId
            OR s.admin IS NULL
        )
        ORDER BY s.name ASC
    """)
    List<SubCaste> searchAvailable(
            String keyword,
            Long adminId
    );
}