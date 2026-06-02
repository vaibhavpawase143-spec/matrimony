package com.example.repository;

import com.example.model.Profile;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository
        extends JpaRepository<Profile, Long>,
        JpaSpecificationExecutor<Profile> {

    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Optional<Profile> findByUser(User user);

    boolean existsByUser(User user);

    List<Profile> findByIsActiveTrue();


    // =====================================================
    // SINGLE PROFILE WITH RELATIONS
    // =====================================================
    @Query("""
SELECT p FROM Profile p
JOIN FETCH p.user

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
LEFT JOIN FETCH p.gender

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

WHERE p.user.id=:userId
AND p.isActive=true
""")
    Optional<Profile> findByUserIdWithRelations(
            Long userId
    );


    // =====================================================
    // ALL PROFILES
    // =====================================================

    @Query("""
SELECT p FROM Profile p
JOIN FETCH p.user

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
LEFT JOIN FETCH p.gender

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
""")
    List<Profile> findAllWithUser();
    // =====================================================
    // ACTIVE PROFILES
    // =====================================================

    @Query("""
SELECT p FROM Profile p
JOIN FETCH p.user

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
LEFT JOIN FETCH p.gender

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

WHERE p.isActive=true
""")
    List<Profile> findActiveProfilesWithUser();
    // =====================================================
// FILTER METHODS
// =====================================================

    List<Profile> findByReligionId(
            Long religionId
    );

    List<Profile> findByCasteId(
            Long casteId
    );

    List<Profile> findByCityId(
            Long cityId
    );

    List<Profile> findByEducationLevelId(
            Long educationLevelId
    );

    List<Profile> findByOccupationId(
            Long occupationId
    );

    List<Profile> findByReligionIdAndCasteId(
            Long religionId,
            Long casteId
    );

    List<Profile> findByCityIdAndEducationLevelId(
            Long cityId,
            Long educationLevelId
    );

    List<Profile> findByOccupationIdAndCityId(
            Long occupationId,
            Long cityId
    );

    List<Profile>
    findByReligionIdAndCityIdAndIsActiveTrue(

            Long religionId,

            Long cityId

    );
}