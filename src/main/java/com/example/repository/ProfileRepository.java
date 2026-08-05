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
    // ✅ BASIC METHODS (no EntityGraph - minimal data)
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
    // ✅ LIGHTWEIGHT QUERIES (for listing profiles)
    // Use case: Search results, profile cards
    // =====================================================

    @EntityGraph(attributePaths = {
        "user",
        "religion", "city"
    })
    @Query("""
        SELECT p FROM Profile p
        WHERE p.isActive = true
        AND p.isDeleted = false
    """)
    Page<Profile> findByIsActiveTrueAndIsDeletedFalse(Pageable pageable);

    // =====================================================
    // ✅ MEDIUM QUERIES (for profile details view)
    // Use case: When user views a profile
    // =====================================================

    @EntityGraph(attributePaths = {
        "user",
        "city", "state", "country",
        "religion", "caste",
        "educationLevel", "occupation",
        "height", "gender", "bodyType",
        "maritalStatus"
    })
    @Query("""
        SELECT p FROM Profile p
        WHERE p.user.id = :userId
        AND p.isActive = true
    """)
    Optional<Profile> findByUserIdWithDetails(@Param("userId") Long userId);

    // =====================================================
    // ✅ FULL QUERIES (for profile editing)

    // Use case: When user edits their own profile
    // Only load when needed (rare operation)
    // =====================================================

    @EntityGraph(attributePaths = {
        "user",
        "city", "state", "country",
        "religion", "caste", "subCaste",
        "educationLevel", "occupation",
        "height", "weight",
        "gender", "bodyType", "complexion",
        "motherTongue", "maritalStatus",
        "income", "diet", "smoking", "drinking",
        "profileType", "manglikStatus",
        "familyType", "familyStatus", "familyValue",
        "qualification", "fieldOfStudy", "employed",
        "disabilityStatus", "bloodGroup"
    })
    @Query("""
        SELECT p FROM Profile p
        WHERE p.user.id = :userId
        AND p.isActive = true
    """)
    Optional<Profile> findByUserIdForEditing(@Param("userId") Long userId);



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
SELECT DISTINCT p
FROM Profile p

JOIN FETCH p.user
LEFT JOIN FETCH p.gender
LEFT JOIN FETCH p.city
LEFT JOIN FETCH p.state
LEFT JOIN FETCH p.country
LEFT JOIN FETCH p.religion
LEFT JOIN FETCH p.caste
LEFT JOIN FETCH p.subCaste
LEFT JOIN FETCH p.educationLevel
LEFT JOIN FETCH p.occupation
LEFT JOIN FETCH p.height
LEFT JOIN FETCH p.weight
LEFT JOIN FETCH p.bodyType
LEFT JOIN FETCH p.complexion
LEFT JOIN FETCH p.motherTongue
LEFT JOIN FETCH p.maritalStatus
LEFT JOIN FETCH p.income
LEFT JOIN FETCH p.diet
LEFT JOIN FETCH p.smoking
LEFT JOIN FETCH p.drinking
LEFT JOIN FETCH p.profileType
LEFT JOIN FETCH p.manglikStatus
LEFT JOIN FETCH p.familyType
LEFT JOIN FETCH p.familyStatus
LEFT JOIN FETCH p.familyValue
LEFT JOIN FETCH p.qualification
LEFT JOIN FETCH p.fieldOfStudy
LEFT JOIN FETCH p.employed
LEFT JOIN FETCH p.disabilityStatus
LEFT JOIN FETCH p.bloodGroup

WHERE p.isActive = true
AND p.profileCompleted = true
AND p.user.isActive = true
AND p.user.isBlocked = false
AND p.user.isDeleted = false
AND p.user.id <> :loggedInUserId

AND LOWER(p.gender.name) = LOWER(:genderName)

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
    List<Profile> findDiscoverProfiles(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("genderName") String genderName
    );
// =====================================================
// AUDIT METHODS
// =====================================================


    Page<Profile> findDiscoverProfilesByGender(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("oppositeGenderId") Long oppositeGenderId,
            Pageable pageable
    );

}