package com.example.repository;

import com.example.model.Profile;
import com.example.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    // ================= BASIC =================

    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Optional<Profile> findByUser(User user);

    // ================= 🔥 FETCH WITH USER =================

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        LEFT JOIN FETCH p.city
        LEFT JOIN FETCH p.religion
        LEFT JOIN FETCH p.caste
    """)
    List<Profile> findAllWithUser();

    // ================= ✅ FIXED ACTIVE =================

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        LEFT JOIN FETCH p.city
        LEFT JOIN FETCH p.religion
        LEFT JOIN FETCH p.caste
        WHERE p.isActive = true
    """)
    List<Profile> findActiveProfilesWithUser();

    // ================= PAGINATION =================

    @Query("""
        SELECT p FROM Profile p
        JOIN p.user u
        WHERE p.isActive = true
        AND u.isActive = true
        AND u.id != :userId
    """)
    Page<Profile> findActiveProfilesExcludingUser(Long userId, Pageable pageable);

    // ================= 🔥 MATCHING =================

    @Query(
            value = """
        SELECT p,

        (
            CASE WHEN (:religionId IS NOT NULL AND p.religion.id = :religionId) THEN 20 ELSE 0 END +
            CASE WHEN (:casteId IS NOT NULL AND p.caste.id = :casteId) THEN 15 ELSE 0 END +
            CASE WHEN (:cityId IS NOT NULL AND p.city.id = :cityId) THEN 15 ELSE 0 END +
            CASE 
                WHEN (:minDob IS NOT NULL AND :maxDob IS NOT NULL 
                      AND p.dateOfBirth BETWEEN :minDob AND :maxDob)
                THEN 20 ELSE 0 
            END
        ) as score

        FROM Profile p
        JOIN FETCH p.user u
        LEFT JOIN FETCH p.city
        LEFT JOIN FETCH p.religion
        LEFT JOIN FETCH p.caste

        WHERE p.isActive = true
        AND u.isActive = true
        AND u.id <> :userId

        ORDER BY score DESC
        """,
            countQuery = """
        SELECT COUNT(p)
        FROM Profile p
        JOIN p.user u
        WHERE p.isActive = true
        AND u.isActive = true
        AND u.id <> :userId
        """
    )
    Page<Object[]> findMatchesWithScorePage(
            Long userId,
            Long religionId,
            Long casteId,
            Long cityId,
            LocalDate minDob,
            LocalDate maxDob,
            Pageable pageable
    );

    // ================= 🔥 FILTER (FIXED WITH FETCH) =================

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.religion.id = :id
    """)
    List<Profile> findByReligionId(Long id);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.caste.id = :id
    """)
    List<Profile> findByCasteId(Long id);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.city.id = :id
    """)
    List<Profile> findByCityId(Long id);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.educationLevel.id = :id
    """)
    List<Profile> findByEducationLevelId(Long id);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.occupation.id = :id
    """)
    List<Profile> findByOccupationId(Long id);

    // ================= COMBINATIONS =================

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.religion.id = :religionId AND p.caste.id = :casteId
    """)
    List<Profile> findByReligionIdAndCasteId(Long religionId, Long casteId);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.city.id = :cityId AND p.educationLevel.id = :educationLevelId
    """)
    List<Profile> findByCityIdAndEducationLevelId(Long cityId, Long educationLevelId);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.occupation.id = :occupationId AND p.city.id = :cityId
    """)
    List<Profile> findByOccupationIdAndCityId(Long occupationId, Long cityId);

    @Query("""
        SELECT p FROM Profile p
        JOIN FETCH p.user
        WHERE p.religion.id = :religionId 
        AND p.city.id = :cityId 
        AND p.isActive = true
    """)
    List<Profile> findByReligionIdAndCityIdAndIsActiveTrue(Long religionId, Long cityId);
}