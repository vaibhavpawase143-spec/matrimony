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

    // ================= ACTIVE =================
    List<Profile> findByIsActiveTrue();

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

    // ================= 🔥 REQUIRED FILTER METHODS =================

    List<Profile> findByReligionId(Long id);

    List<Profile> findByCasteId(Long id);

    List<Profile> findByCityId(Long id);

    List<Profile> findByEducationLevelId(Long id);

    List<Profile> findByOccupationId(Long id);

    List<Profile> findByReligionIdAndCasteId(Long religionId, Long casteId);

    List<Profile> findByCityIdAndEducationLevelId(Long cityId, Long educationLevelId);

    List<Profile> findByOccupationIdAndCityId(Long occupationId, Long cityId);

    List<Profile> findByReligionIdAndCityIdAndIsActiveTrue(Long religionId, Long cityId);
}