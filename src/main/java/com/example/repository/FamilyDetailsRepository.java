package com.example.repository;

import com.example.model.FamilyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    // 🔍 Find by profile (VERY IMPORTANT - OneToOne)
    Optional<FamilyDetails> findByProfileId(Long profileId);

    // 🔍 Check if profile already has family details
    boolean existsByProfileId(Long profileId);

    // 🔍 Filter by family type
    List<FamilyDetails> findByFamilyTypeId(Long familyTypeId);

    // 🔍 Filter by family status
    List<FamilyDetails> findByFamilyStatusId(Long familyStatusId);

    // 🔍 Filter by brother type
    List<FamilyDetails> findByBrotherTypeId(Long brotherTypeId);

    // 🔍 Filter by sister type
    List<FamilyDetails> findBySisterTypeId(Long sisterTypeId);
}