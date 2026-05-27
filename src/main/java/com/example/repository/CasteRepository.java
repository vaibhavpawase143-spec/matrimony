package com.example.repository;

import com.example.model.Caste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasteRepository extends JpaRepository<Caste, Long> {

    // =========================================
    // FIND
    // =========================================

    Optional<Caste> findByNameIgnoreCase(String name);

    // =========================================
    // SECURE FETCH
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE c.id = :id
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
    """)
    Optional<Caste> findAccessibleById(
            Long id,
            Long adminId
    );

    // =========================================
    // DUPLICATE CHECK
    // =========================================

    @Query("""
        SELECT COUNT(c) > 0
        FROM Caste c
        WHERE LOWER(c.name) = LOWER(:name)
        AND c.religion.id = :religionId
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
    """)
    boolean existsAvailableCaste(
            String name,
            Long religionId,
            Long adminId
    );

    // =========================================
    // GET ALL
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
        ORDER BY c.name ASC
    """)
    List<Caste> findAllAvailable(Long adminId);

    // =========================================
    // GET ACTIVE
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE c.isActive = true
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
        ORDER BY c.name ASC
    """)
    List<Caste> findAllActiveAvailable(Long adminId);

    // =========================================
    // RELIGION FILTER
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE c.religion.id = :religionId
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
        ORDER BY c.name ASC
    """)
    List<Caste> findAvailableByReligion(
            Long religionId,
            Long adminId
    );

    // =========================================
    // ACTIVE + RELIGION
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE c.religion.id = :religionId
        AND c.isActive = true
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
        ORDER BY c.name ASC
    """)
    List<Caste> findActiveAvailableByReligion(
            Long religionId,
            Long adminId
    );

    // =========================================
    // SEARCH
    // =========================================

    @Query("""
        SELECT c FROM Caste c
        WHERE LOWER(c.name)
        LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND (
            c.admin.id = :adminId
            OR c.admin IS NULL
        )
        ORDER BY c.name ASC
    """)
    List<Caste> searchAvailable(
            String keyword,
            Long adminId
    );
}