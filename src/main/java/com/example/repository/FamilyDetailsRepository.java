package com.example.repository;

import com.example.model.FamilyDetails;
import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {
    FamilyDetails findByProfile(Profile profile);


    // 🔍 One-to-One mapping (Profile ↔ FamilyDetails)
    Optional<FamilyDetails> findByProfile_Id(Long profileId);

    boolean existsByProfile_Id(Long profileId);

    // 🔍 FamilyType filtering
    List<FamilyDetails> findByFamilyType_Id(Long familyTypeId);

    // 🔍 Family filtering
    List<FamilyDetails> findByFamily_Id(Long familyId);

    // 🔥 Family + Active (ONLY if Family has isActive field)
    List<FamilyDetails> findByFamily_IdAndFamily_IsActiveTrue(Long familyId);

    // 🔍 BrotherType filtering
    List<FamilyDetails> findByBrotherType_Id(Long brotherTypeId);

    // 🔍 SisterType filtering
    List<FamilyDetails> findBySisterType_Id(Long sisterTypeId);

    // 🔥 Optional: Profile + Active (if FamilyDetails has isActive)
    List<FamilyDetails> findByProfile_IdAndIsActiveTrue(Long profileId);
}