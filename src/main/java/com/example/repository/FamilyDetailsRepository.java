package com.example.repository;

import com.example.model.FamilyDetails;
import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    // =========================
    // Get By ID
    // =========================

    Optional<FamilyDetails> findByIdAndDeletedAtIsNull(Long id);

    // =========================
    // Get All
    // =========================

    List<FamilyDetails> findAllByDeletedAtIsNull();

    // =========================
    // Profile
    // =========================

    FamilyDetails findByProfileAndDeletedAtIsNull(Profile profile);

    Optional<FamilyDetails> findByProfile_IdAndDeletedAtIsNull(Long profileId);

    boolean existsByProfile_IdAndDeletedAtIsNull(Long profileId);

    // =========================
    // Family Type
    // =========================

    List<FamilyDetails> findByFamilyType_IdAndDeletedAtIsNull(Long familyTypeId);

    // =========================
    // Family
    // =========================

    List<FamilyDetails> findByFamily_IdAndDeletedAtIsNull(Long familyId);

    List<FamilyDetails> findByFamily_IdAndFamily_IsActiveTrueAndDeletedAtIsNull(Long familyId);

    // =========================
    // Brother Type
    // =========================

    List<FamilyDetails> findByBrotherType_IdAndDeletedAtIsNull(Long brotherTypeId);

    // =========================
    // Sister Type
    // =========================

    List<FamilyDetails> findBySisterType_IdAndDeletedAtIsNull(Long sisterTypeId);

    // =========================
    // Profile Active
    // =========================

    List<FamilyDetails> findByProfile_IdAndIsActiveTrueAndDeletedAtIsNull(Long profileId);

    // =========================
    // Soft Deleted
    // =========================

    List<FamilyDetails> findByDeletedAtIsNotNull();

    Optional<FamilyDetails> findByIdAndDeletedAtIsNotNull(Long id);
}