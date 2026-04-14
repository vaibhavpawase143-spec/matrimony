package com.example.repository;

import com.example.model.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // ================= AUTH =================

    Optional<User> findByEmailIgnoreCaseAndIsActiveTrue(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);

    // ================= VALIDATION =================

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    boolean existsByEmailIgnoreCaseOrPhone(String email, String phone);

    // ================= BASIC =================

    List<User> findByIsActiveTrue();

    Optional<User> findByIdAndIsActiveTrue(Long id);

    // ================= SEARCH =================

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );

    List<User> findByEmailContainingIgnoreCase(String keyword);

    // ================= 🔥 ROLE FETCH (VERY IMPORTANT) =================

    // ✅ FIXED: ensures roles always load for JWT
    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE LOWER(u.email) = LOWER(:email)
    """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    // ✅ FIXED: prevents duplicate users
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.roles
    """)
    List<User> findAllWithRoles();

    // ✅ FIXED: fetch roles with user
    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE u.id = :id
    """)
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    // ✅ FIXED: active users with roles
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE u.isActive = true
    """)
    List<User> findActiveUsersWithRoles();

    // ✅ BEST PRACTICE: search WITH roles (use this in service 🔥)
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<User> searchWithRoles(@Param("keyword") String keyword);

    // ================= ONLINE STATUS =================

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE User u
        SET u.isOnline = :isOnline,
            u.lastSeen = :lastSeen
        WHERE u.email = :email
    """)
    void updateUserStatus(
            @Param("email") String email,
            @Param("isOnline") Boolean isOnline,
            @Param("lastSeen") LocalDateTime lastSeen
    );

    // ================= MATCH =================

    @Query("""
        SELECT u FROM User u
        WHERE u.id != :userId
        AND u.isActive = true
        ORDER BY u.createdAt DESC
    """)
    List<User> findTopMatches(
            @Param("userId") Long userId,
            Pageable pageable
    );
    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.profile p
    LEFT JOIN FETCH p.city
    LEFT JOIN FETCH p.religion
    LEFT JOIN FETCH p.caste
    WHERE u.id = :id
""")
    Optional<User> findByIdWithProfile(@Param("id") Long id);
}