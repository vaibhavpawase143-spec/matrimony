package com.example.repository;

import com.example.model.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Religion entity.
 * Includes both soft-deleted and non-deleted record queries.
 */
@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    // ================= NON-DELETED QUERIES =================

    /**
     * Find active religions (non-deleted and isActive=true)
     */
    @Query("SELECT r FROM Religion r WHERE r.deletedAt IS NULL AND r.isActive = true ORDER BY r.name ASC")
    List<Religion> findAllActive();

    /**
     * Find active religions by admin
     */
    @Query("SELECT r FROM Religion r WHERE r.admin.id = :adminId AND r.deletedAt IS NULL AND r.isActive = true ORDER BY r.name ASC")
    List<Religion> findActiveByAdmin(@Param("adminId") Long adminId);

    /**
     * Find religion by name (case-insensitive, non-deleted)
     */
    @Query("SELECT r FROM Religion r WHERE LOWER(r.name) = LOWER(:name) AND r.deletedAt IS NULL")
    Optional<Religion> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find by name and admin (case-insensitive, non-deleted)
     */
    @Query("SELECT r FROM Religion r WHERE LOWER(r.name) = LOWER(:name) AND r.admin.id = :adminId AND r.deletedAt IS NULL")
    Optional<Religion> findByNameIgnoreCaseAndAdmin(@Param("name") String name, @Param("adminId") Long adminId);

    /**
     * Check if religion exists for admin (non-deleted)
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Religion r WHERE LOWER(r.name) = LOWER(:name) AND r.admin.id = :adminId AND r.deletedAt IS NULL")
    boolean existsByNameIgnoreCaseAndAdmin(@Param("name") String name, @Param("adminId") Long adminId);

    /**
     * Get all non-deleted records by admin
     */
    List<Religion> findByAdminIdAndDeletedAtIsNull(Long adminId);

    /**
     * Get active non-deleted records by admin
     */
    List<Religion> findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    /**
     * Get inactive non-deleted records by admin
     */
    List<Religion> findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    /**
     * Search non-deleted records by admin and keyword
     */
    @Query("SELECT r FROM Religion r WHERE r.admin.id = :adminId AND r.deletedAt IS NULL AND LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.name ASC")
    List<Religion> searchByAdminAndKeyword(@Param("adminId") Long adminId, @Param("keyword") String keyword);

    // ================= SOFT DELETE QUERIES =================

    /**
     * Find deleted records by admin
     */
    List<Religion> findByAdminIdAndDeletedAtIsNotNull(Long adminId);

    /**
     * Check if record is deleted
     */
    @Query("SELECT CASE WHEN r.deletedAt IS NOT NULL THEN true ELSE false END FROM Religion r WHERE r.id = :id")
    boolean isDeleted(@Param("id") Long id);

    // ================= BACKWARD COMPATIBILITY (DEPRECATED) =================
    /**
     * @deprecated Use findByNameIgnoreCaseAndAdmin instead
     */
    @Deprecated
    Optional<Religion> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    /**
     * @deprecated Use existsByNameIgnoreCaseAndAdmin instead
     */
    @Deprecated
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    /**
     * @deprecated Use findByAdminIdAndDeletedAtIsNull instead
     */
    @Deprecated
    List<Religion> findByAdminId(Long adminId);

    /**
     * @deprecated Use findByAdminIdAndIsActiveTrueAndDeletedAtIsNull instead
     */
    @Deprecated
    List<Religion> findByAdminIdAndIsActiveTrue(Long adminId);

    /**
     * @deprecated Use findByAdminIdAndIsActiveFalseAndDeletedAtIsNull instead
     */
    @Deprecated
    List<Religion> findByAdminIdAndIsActiveFalse(Long adminId);

    /**
     * @deprecated Use searchByAdminAndKeyword instead
     */
    @Deprecated
    List<Religion> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}