package com.example.repository;

import com.example.model.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    // ================= VERIFICATION =================

    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.isDeleted = false")
    Page<User> findByEmailVerifiedFalseAndIsDeletedFalse(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.phoneVerified = false AND u.isDeleted = false")
    Page<User> findByPhoneVerifiedFalseAndIsDeletedFalse(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = false AND u.isDeleted = false")
    Long countByEmailVerifiedFalseAndIsDeletedFalse();

    @Query("SELECT COUNT(u) FROM User u WHERE u.phoneVerified = false AND u.isDeleted = false")
    Long countByPhoneVerifiedFalseAndIsDeletedFalse();

    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = true AND u.isDeleted = false")
    Long countByEmailVerifiedTrueAndIsDeletedFalse();

    @Query("SELECT COUNT(u) FROM User u WHERE u.phoneVerified = true AND u.isDeleted = false")
    Long countByPhoneVerifiedTrueAndIsDeletedFalse();

    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = true AND u.phoneVerified = true AND u.isDeleted = false")
    Long countByEmailVerifiedTrueAndPhoneVerifiedTrueAndIsDeletedFalse();

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

    @Transactional
    @Modifying
    @Query("""
UPDATE User u
SET u.lastHeartbeat = :time,
    u.isOnline = true
WHERE u.email = :email
""")
     void updateHeartbeat(
            @Param("email") String email,
            @Param("time") LocalDateTime time
    );

    @Query("""
SELECT u
FROM User u
WHERE u.isOnline = true
AND u.lastHeartbeat < :time
""")
    List<User> findExpiredUsers(
            @Param("time") LocalDateTime time
    );
    // ================= MATCH =================

    @Query("""
    SELECT u FROM User u
    WHERE u.id != :userId
      AND u.isActive = true
      AND u.isDeleted = false
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
      AND u.isDeleted = false
""")
    Optional<User> findByIdWithProfile(@Param("id") Long id);
    // ================= ADMIN DASHBOARD QUERIES =================

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt > :date")
    long findNewUsersCount(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM User u WHERE CAST(u.createdAt AS DATE) = CAST(:date AS DATE)")
    long findUsersCountByDate(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastSeen > :date AND u.isActive = true")
    long countActiveSince(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.profile IS NOT NULL")
    long countUsersWithProfile();

    @Query("SELECT COUNT(u) FROM User u WHERE u.profile IS NOT NULL AND u.profile.dateOfBirth IS NOT NULL")
    long countUsersWithCompletedProfile();

    @Query("SELECT r.name, COUNT(u) FROM User u LEFT JOIN u.profile p LEFT JOIN p.religion r GROUP BY r.name")
    Map<String, Long> countUsersByReligion();

    @Query("SELECT c.name, COUNT(u) FROM User u LEFT JOIN u.profile p LEFT JOIN p.city c GROUP BY c.name")
    Map<String, Long> countUsersByCity();

    @Query("SELECT p.gender, COUNT(u) FROM User u LEFT JOIN u.profile p GROUP BY p.gender")
    Map<String, Long> countUsersByGender();

    @Query("SELECT e.name, COUNT(u) FROM User u LEFT JOIN u.profile p LEFT JOIN p.educationLevel e GROUP BY e.name")
    Map<String, Long> countUsersByEducation();
    @Query("""
    SELECT DISTINCT u
    FROM User u
    LEFT JOIN FETCH u.profile p
    LEFT JOIN FETCH p.gender
    LEFT JOIN FETCH p.religion
    LEFT JOIN FETCH p.caste
    LEFT JOIN FETCH p.city
    LEFT JOIN FETCH p.educationLevel
    LEFT JOIN FETCH p.maritalStatus
    WHERE u.isDeleted = false
    ORDER BY u.createdAt DESC
""")
    List<User> findAllUsersWithProfile();
    @Query("""
SELECT u
FROM User u
LEFT JOIN FETCH u.profile
WHERE u.isActive = true
AND u.isDeleted = false
""")
    List<User> findActiveUsersWithProfile();

    @Query("""
SELECT DISTINCT u
FROM User u
LEFT JOIN FETCH u.profile p
WHERE u.isDeleted = false
AND (
    LOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
)
ORDER BY u.createdAt DESC
""")
    List<User> searchUsers(@Param("keyword") String keyword);

    @Query(
            value = """
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.profile p
        WHERE u.isDeleted = false
        """,
            countQuery = """
        SELECT COUNT(u)
        FROM User u
        WHERE u.isDeleted = false
        """
    )
    Page<User> findAllUsersWithProfile(Pageable pageable);
    // ================= ADMIN USER MANAGEMENT =================
    Long countByIsActiveTrueAndIsDeletedFalse();
    Long countByIsBlockedTrueAndIsDeletedFalse();
    Long countByIsDeletedTrue();
    Long countByIsDeletedFalse();

    Long countByIsActiveFalseAndIsDeletedFalse();

    // ==========================================
// DASHBOARD - MONTHLY USER REGISTRATIONS
// ==========================================

    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM') AS month,
               COUNT(*) AS total
        FROM users
        WHERE is_deleted = false
        GROUP BY TO_CHAR(created_at, 'YYYY-MM')
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> getMonthlyUserRegistrations();

    // ==========================================
// DASHBOARD - MONTHLY REPORTS
// ==========================================

    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM') AS month,
               COUNT(*) AS total
        FROM user_reports
        GROUP BY TO_CHAR(created_at, 'YYYY-MM')
        ORDER BY month
        """, nativeQuery = true)
    List<Object[]> getMonthlyReports();
    // ==========================================
// DASHBOARD - TOP CITIES
// ==========================================

    @Query(value = """
SELECT
    c.id,
    c.name,
    COUNT(p.id)
FROM profiles p
JOIN cities c
ON p.city_id = c.id
GROUP BY c.id,c.name
ORDER BY COUNT(p.id) DESC
LIMIT 10
""", nativeQuery = true)
    List<Object[]> getTopCities();
    // ==========================================
// DASHBOARD - TOP RELIGIONS
// ==========================================

    @Query(value = """
SELECT
    r.id,
    r.name,
    COUNT(p.id)
FROM profiles p
JOIN religions r
ON p.religion_id=r.id
GROUP BY r.id,r.name
ORDER BY COUNT(p.id) DESC
LIMIT 10
""", nativeQuery = true)
    List<Object[]> getTopReligions();

    @Query(value = """
SELECT COUNT(*)
FROM users
WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE)
AND is_deleted = false
""", nativeQuery = true)
    Long countCurrentMonthUsers();

    @Query(value = """
SELECT COUNT(*)
FROM users
WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
AND created_at < DATE_TRUNC('month', CURRENT_DATE)
AND is_deleted = false
""", nativeQuery = true)
    Long countPreviousMonthUsers();

}