package com.example.repository;

import com.example.model.FamilyDetails;
import com.example.model.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    // =========================
    // Get By ID
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    Optional<FamilyDetails> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // Get All
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findAllByDeletedAtIsNull();

    // =========================
    // Profile
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    FamilyDetails findByProfileAndDeletedAtIsNull(Profile profile);

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    Optional<FamilyDetails> findByProfile_IdAndDeletedAtIsNull(Long profileId);

    boolean existsByProfile_IdAndDeletedAtIsNull(Long profileId);

    // =========================
    // Family Type
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findByFamilyType_IdAndDeletedAtIsNull(Long familyTypeId);

    // =========================
    // Family
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findByFamily_IdAndDeletedAtIsNull(Long familyId);

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findByFamily_IdAndFamily_IsActiveTrueAndDeletedAtIsNull(Long familyId);

    // =========================
    // Brother Type
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findByBrotherType_IdAndDeletedAtIsNull(Long brotherTypeId);

    // =========================
    // Sister Type
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findBySisterType_IdAndDeletedAtIsNull(Long sisterTypeId);

    // =========================
    // Profile Active
    // =========================

    @EntityGraph(attributePaths = {
            "profile",
            "familyType",
            "family",
            "brotherType",
            "sisterType"
    })
    List<FamilyDetails> findByProfile_IdAndIsActiveTrueAndDeletedAtIsNull(Long profileId);

    // =========================
    // Soft Deleted
    // =========================

    List<FamilyDetails> findByDeletedAtIsNotNull();

    Optional<FamilyDetails> findByIdAndDeletedAtIsNotNull(Long id);
}