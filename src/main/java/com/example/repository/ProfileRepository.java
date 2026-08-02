package com.example.repository;


import com.example.model.Profile;
import com.example.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface ProfileRepository extends

        JpaRepository<Profile, Long>,

        JpaSpecificationExecutor<Profile> {



    // =====================================================

    // BASIC METHODS

    // =====================================================



    Optional<Profile> findByUserId(Long userId);



    Optional<Profile> findByUser(User user);



    boolean existsByUserId(Long userId);



    boolean existsByUser(User user);



    List<Profile> findByIsActiveTrue();



    List<Profile> findByIsDeletedFalse();







    List<Profile> findByCreatedBy(Long createdBy);



    List<Profile> findByUpdatedBy(Long updatedBy);



    // =====================================================

    // PROFILE DETAILS

    // Used by:

    // - My Profile

    // - View Profile

    // - Edit Profile

    // =====================================================



    @EntityGraph(attributePaths = {



            "user",



            "city",

            "state",

            "country",



            "religion",

            "caste",

            "subCaste",



            "educationLevel",

            "occupation",



            "height",

            "weight",



            "gender",



            "bodyType",

            "complexion",



            "motherTongue",

            "maritalStatus",



            "income",



            "diet",

            "smoking",

            "drinking",



            "profileType",

            "manglikStatus",



            "familyType",

            "familyStatus",

            "familyValue",



            "qualification",

            "fieldOfStudy",

            "employed",



            "disabilityStatus",

            "bloodGroup"



    })

    @Query("""



            SELECT p

            FROM Profile p



            WHERE p.user.id = :userId

            AND p.isActive = true



            """)

    Optional<Profile> findByUserIdWithRelations(Long userId);



    // =====================================================

    // PROFILE DETAILS BY PROFILE ID

    // =====================================================



    @EntityGraph(attributePaths = {



            "user",



            "city",

            "state",

            "country",



            "religion",

            "caste",

            "subCaste",



            "educationLevel",

            "occupation",



            "height",

            "weight",



            "gender",



            "bodyType",

            "complexion",



            "motherTongue",

            "maritalStatus",



            "income",



            "diet",

            "smoking",

            "drinking",



            "profileType",

            "manglikStatus",



            "familyType",

            "familyStatus",

            "familyValue",



            "qualification",

            "fieldOfStudy",

            "employed",



            "disabilityStatus",

            "bloodGroup"



    })

    @Query("""



            SELECT p

            FROM Profile p



            WHERE p.id = :profileId

            AND p.isActive = true



            """)

    Optional<Profile> findByProfileIdWithRelations(Long profileId);

    // =====================================================

// DISCOVER PROFILES (OPTIMIZED)

// =====================================================



    @EntityGraph(attributePaths = {"user", "city", "religion", "occupation", "height", "gender", "user", "gender", "religion", "caste", "city", "occupation", "height"})
    @Query("""
SELECT p
FROM Profile p
WHERE p.isActive = true
AND p.profileCompleted = true
AND p.user.isActive = true
AND p.user.isBlocked = false
AND p.user.isDeleted = false


AND p.user.id <> :loggedInUserId

AND p.user.id NOT IN (
    SELECT ub.blockedId
    FROM UserBlock ub
    WHERE ub.blockerId = :loggedInUserId
    AND ub.isActive = true
)
AND p.user.id NOT IN (
        SELECT ub.blockedId
        FROM UserBlock ub
        WHERE ub.blockerId = :loggedInUserId
        AND ub.isActive = true
)

ORDER BY
    p.isPremium DESC,
    p.boostScore DESC,
    p.createdAt DESC
""")
    Page<Profile> findDiscoverProfiles(
            @Param("loggedInUserId") Long loggedInUserId,
            Pageable pageable
    );





// =====================================================

// DISCOVER (WITHOUT BLOCK FILTER)

// Used by Admin

// =====================================================



    @EntityGraph(attributePaths = {

            "user",

            "city",

            "religion",

            "occupation",

            "height",

            "gender"

    })

    @Query("""



        SELECT p



        FROM Profile p



        WHERE p.isActive = true



        AND p.profileCompleted = true



        AND p.user.isActive = true



        AND p.user.isBlocked = false



        AND p.user.isDeleted = false



        """)

    Page<Profile> findAllWithUser(

            Pageable pageable

    );







// =====================================================

// MATCHING ENGINE

// =====================================================



    @EntityGraph(attributePaths = {

            "user",

            "city",

            "religion",

            "caste",

            "educationLevel",

            "occupation",

            "height",

            "weight",

            "maritalStatus",

            "smoking",

            "drinking",

            "diet",

            "gender"

    })

    @Query("""



        SELECT p



        FROM Profile p



        WHERE p.isActive = true



        AND p.profileCompleted = true



        AND p.user.isActive = true



        AND p.user.isBlocked = false



        AND p.user.isDeleted = false



        """)

    Page<Profile> findAllEligibleForMatching(

            Pageable pageable

    );







// =====================================================

// PREMIUM USERS

// =====================================================



    @EntityGraph(attributePaths = {

            "user",

            "city",

            "occupation"

    })

    Page<Profile> findByIsPremiumTrue(

            Pageable pageable

    );







// =====================================================

// NEWEST USERS

// =====================================================



    @EntityGraph(attributePaths = {

            "user",

            "city"

    })

    Page<Profile> findByIsActiveTrueOrderByCreatedAtDesc(

            Pageable pageable

    );







// =====================================================

// VERIFIED USERS

// =====================================================



    @EntityGraph(attributePaths = {

            "user"

    })

    @Query("""



SELECT p



FROM Profile p



WHERE p.user.emailVerified = true



AND p.user.phoneVerified = true



AND p.user.isActive = true



""")

    Page<Profile> findVerifiedProfiles(

            Pageable pageable

    );

    // =====================================================

    // FILTER METHODS

    // =====================================================



    Page<Profile> findByReligionId(

            Long religionId,

            Pageable pageable

    );



    Page<Profile> findByCasteId(

            Long casteId,

            Pageable pageable

    );



    Page<Profile> findByCityId(

            Long cityId,

            Pageable pageable

    );



    Page<Profile> findByEducationLevelId(

            Long educationLevelId,

            Pageable pageable

    );



    Page<Profile> findByOccupationId(

            Long occupationId,

            Pageable pageable

    );



    Page<Profile> findByReligionIdAndCasteId(

            Long religionId,

            Long casteId,

            Pageable pageable

    );



    Page<Profile> findByCityIdAndEducationLevelId(

            Long cityId,

            Long educationLevelId,

            Pageable pageable

    );



    Page<Profile> findByOccupationIdAndCityId(

            Long occupationId,

            Long cityId,

            Pageable pageable

    );



    Page<Profile> findByReligionIdAndCityIdAndIsActiveTrue(

            Long religionId,

            Long cityId,

            Pageable pageable

    );







    // =====================================================

    // ADMIN

    // =====================================================



    @EntityGraph(attributePaths = {

            "user",

            "city",

            "religion",

            "occupation"

    })

    Page<Profile> findByIsActiveTrue(

            Pageable pageable

    );







    @EntityGraph(attributePaths = {

            "user"

    })

    Page<Profile> findByProfileCompletedTrue(

            Pageable pageable

    );







    @EntityGraph(attributePaths = {

            "user"

    })

    Page<Profile> findByIsPremiumTrueAndIsActiveTrue(

            Pageable pageable

    );







    // =====================================================

    // AUDIT

    // =====================================================



    Page<Profile> findByCreatedBy(

            Long createdBy,

            Pageable pageable

    );







    Page<Profile> findByUpdatedBy(

            Long updatedBy,

            Pageable pageable

    );







    Page<Profile> findByIsDeletedFalse(

            Pageable pageable

    );







    Optional<Profile> findByUser_IdAndIsDeletedFalse(

            Long userId

    );







    // =====================================================

    // EXISTS

    // =====================================================



    boolean existsByUserIdAndIsDeletedFalse(

            Long userId

    );







    boolean existsByUserIdAndIsActiveTrue(

            Long userId

    );







    // =====================================================

    // COUNT

    // =====================================================



    long countByIsActiveTrue();







    long countByProfileCompletedTrue();







    long countByIsPremiumTrue();







    long countByCreatedAtBetween(

            LocalDateTime start,

            LocalDateTime end

    );







    // =====================================================

    // DASHBOARD

    // =====================================================



    @Query("""



            SELECT COUNT(p)



            FROM Profile p



           WHERE p.gender.id = :genderId



            AND p.isActive = true



            """)

    long countByGender(Long genderId);







    @Query("""



            SELECT COUNT(p)



            FROM Profile p



            WHERE p.city.id = :cityId



            """)

    long countByCity(Long cityId);







    @Query("""



            SELECT COUNT(p)



            FROM Profile p



            WHERE p.religion.id = :religionId



            """)

    long countByReligion(Long religionId);

    @EntityGraph(attributePaths = {
            "user",
            "gender",
            "religion",
            "caste",
            "city",
            "occupation",
            "height"
    })
    @Query("""
SELECT p
FROM Profile p
WHERE p.isActive = true
AND p.profileCompleted = true
AND p.user.isActive = true
AND p.user.isBlocked = false
AND p.user.isDeleted = false
AND p.user.id <> :loggedInUserId
AND p.gender.id = :oppositeGenderId
AND p.user.id NOT IN (
    SELECT ub.blockedId
    FROM UserBlock ub
    WHERE ub.blockerId = :loggedInUserId
    AND ub.isActive = true
)
ORDER BY
    p.isPremium DESC,
    p.boostScore DESC,
    p.createdAt DESC
""")
    Page<Profile> findDiscoverProfilesByGender(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("oppositeGenderId") Long oppositeGenderId,
            Pageable pageable
    );

}