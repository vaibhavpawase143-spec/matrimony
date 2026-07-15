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
    // GET BY ID
    // =========================================

    @Query("""
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE c.id = :id
          AND c.deletedAt IS NULL
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
          AND c.deletedAt IS NULL
    """)
    boolean existsByNameIgnoreCaseAndReligionIdAndDeletedAtIsNull(
            String name,
            Long religionId
    );

    // =========================================
    // GET ALL
    // =========================================

    @Query("""
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE c.deletedAt IS NULL
          AND (
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
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE c.deletedAt IS NULL
          AND c.isActive = true
          AND (
                c.admin.id = :adminId
                OR c.admin IS NULL
          )
        ORDER BY c.name ASC
    """)
    List<Caste> findAllActiveAvailable(Long adminId);

    // =========================================
    // GET BY RELIGION
    // =========================================

    @Query("""
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE c.religion.id = :religionId
          AND c.deletedAt IS NULL
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
    // GET ACTIVE BY RELIGION
    // =========================================

    @Query("""
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE c.religion.id = :religionId
          AND c.deletedAt IS NULL
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
        SELECT c
        FROM Caste c
        LEFT JOIN FETCH c.religion
        WHERE LOWER(c.name)
              LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND c.deletedAt IS NULL
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

    // =========================================
    // ADMIN METHODS
    // =========================================

    List<Caste> findByAdminIdAndDeletedAtIsNull(Long adminId);

    List<Caste> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    List<Caste> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);
}