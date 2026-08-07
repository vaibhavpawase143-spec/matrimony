package com.example.repository;

import com.example.model.Admin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends
        JpaRepository<Admin, Long>,
        JpaSpecificationExecutor<Admin> {

    // ==========================================================
    // LOGIN
    // ==========================================================

    Optional<Admin> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT a
            FROM Admin a
            WHERE UPPER(a.email) = UPPER(:email)
            """)
    Optional<Admin> findByEmailWithRole(@Param("email") String email);

    // ==========================================================
    // LOOKUP
    // ==========================================================

    Optional<Admin> findByUsername(String username);

    /**
     * Keep this method because it is used throughout the service layer.
     * Removing it would require changes in multiple services.
     */
    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT a
            FROM Admin a
            WHERE a.id = :id
            """)
    Optional<Admin> findByIdWithRole(@Param("id") Long id);

    /**
     * Keep default JpaRepository findById()
     */
    @Override
    @EntityGraph(attributePaths = "role")
    Optional<Admin> findById(Long id);

    // ==========================================================
    // VALIDATION
    // ==========================================================

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    // ==========================================================
    // FETCH
    // ==========================================================

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT a
            FROM Admin a
            """)
    List<Admin> findAllWithRole();

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT DISTINCT a
            FROM Admin a
            WHERE
                LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY a.createdAt DESC
            """)
    List<Admin> searchAdmins(@Param("keyword") String keyword);

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT a
            FROM Admin a
            WHERE a.isActive = true
            ORDER BY a.createdAt DESC
            """)
    List<Admin> findAllActiveAdmins();

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT a
            FROM Admin a
            WHERE a.isActive = :isActive
            ORDER BY a.createdAt DESC
            """)
    List<Admin> findByIsActiveWithRole(@Param("isActive") Boolean isActive);

    // ==========================================================
    // DASHBOARD
    // ==========================================================

    long countByIsActiveTrue();

    long countByIsActiveFalse();

    @Query("""
            SELECT COUNT(a)
            FROM Admin a
            WHERE a.createdAt >= :startDate
            """)
    long countNewAdmins(@Param("startDate") LocalDateTime startDate);

    @Query("""
            SELECT COUNT(a)
            FROM Admin a
            WHERE a.role.name = :roleName
            """)
    long countByRole(@Param("roleName") String roleName);

    @Query("""
            SELECT COUNT(a)
            FROM Admin a
            WHERE a.role.name = 'ROLE_SUPER_ADMIN'
            AND a.isActive = true
            """)
    long countActiveSuperAdmins();
}