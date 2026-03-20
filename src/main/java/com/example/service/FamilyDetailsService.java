package com.example.service;

import com.example.model.FamilyDetails;

import java.util.List;
import java.util.Optional;

public interface FamilyDetailsService {

    FamilyDetails saveFamilyDetails(FamilyDetails familyDetails);

    Optional<FamilyDetails> getByProfileId(Long profileId);

    boolean existsByProfileId(Long profileId);

    List<FamilyDetails> getByFamilyType(Long familyTypeId);

    List<FamilyDetails> getByFamilyStatus(Long FamilyStatusId);

    List<FamilyDetails> getByBrotherType(Long brotherTypeId);

    List<FamilyDetails> getBySisterType(Long sisterTypeId);

    FamilyDetails updateFamilyDetails(Long profileId, FamilyDetails updatedDetails);

    void deleteById(Long id);
}