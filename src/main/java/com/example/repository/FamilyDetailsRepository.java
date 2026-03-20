package com.example.repository;

import com.example.model.FamilyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    // 🔍 Find by profile (OneToOne relationship)
    Optional<FamilyDetails> findByProfile_Id(Long profileId);

    // 🔍 Check if profile already has family details
    boolean existsByProfile_Id(Long profileId);

    // 🔍 Filter by family type
    List<FamilyDetails> findByFamilyType_Id(Long familyTypeId);

    // ✅ FIXED (removed familyStatus, using isActive instead)
    List<FamilyDetails> findByFamily_IsActiveTrue();

    // 🔍 Filter by family id
    List<FamilyDetails> findByFamily_Id(Long familyId);

    // 🔍 Filter by family id + active
    List<FamilyDetails> findByFamily_IdAndFamily_IsActiveTrue(Long familyId);

    // 🔍 Filter by brother type
    List<FamilyDetails> findByBrotherType_Id(Long brotherTypeId);

    // 🔍 Filter by sister type
    List<FamilyDetails> findBySisterType_Id(Long sisterTypeId);
}