package com.example.repository;

import com.example.model.Admin;
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

    // ================= 🔐 LOGIN =================

    // Keep this (fallback)
    Optional<Admin> findByEmailIgnoreCase(String email);

    // ✅ MAIN METHOD (USE THIS FOR LOGIN)
    @Query("SELECT a FROM Admin a JOIN FETCH a.role WHERE upper(a.email) = upper(:email)")
    Optional<Admin> findByEmailWithRole(@Param("email") String email);


    // ================= 🔍 OPTIONAL =================

    Optional<Admin> findByUsername(String username);


    // ================= ✅ VALIDATION =================

    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsername(String username);


    // ================= 🔥 FETCH HELPERS =================

    @Query("SELECT a FROM Admin a JOIN FETCH a.role WHERE a.id = :id")
    Optional<Admin> findByIdWithRole(@Param("id") Long id);

    @Query("SELECT a FROM Admin a JOIN FETCH a.role")
    List<Admin> findAllWithRole();
    long countByIsActiveTrue();

    long countByIsActiveFalse();
    @Query("""
SELECT COUNT(a)
FROM Admin a
WHERE a.createdAt >= :startDate
""")
    long countNewAdmins(
            @Param("startDate") LocalDateTime startDate
    );
    @Query("""
SELECT COUNT(a)
FROM Admin a
WHERE a.role.name = :roleName
""")
    long countByRole(
            @Param("roleName") String roleName
    );
    @Query("""
SELECT DISTINCT a
FROM Admin a
JOIN FETCH a.role
WHERE
LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Admin> searchAdmins(
            @Param("keyword") String keyword
    );
    @Query("""
SELECT DISTINCT a
FROM Admin a
JOIN FETCH a.role
WHERE a.isActive = true
ORDER BY a.createdAt DESC
""")
    List<Admin> findAllActiveAdmins();
    @Query("""
SELECT a
FROM Admin a
JOIN FETCH a.role
WHERE a.isActive = :isActive
""")
    List<Admin> findByIsActiveWithRole(
            @Param("isActive") Boolean isActive
    );
    @Query("""
SELECT COUNT(a)
FROM Admin a
WHERE a.role.name='ROLE_SUPER_ADMIN'
AND a.isActive=true
""")
    long countActiveSuperAdmins();

    boolean existsByPhoneAndIdNot(
            String phone,
            Long id
    );
}