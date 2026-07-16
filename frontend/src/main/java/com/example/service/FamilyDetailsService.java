package com.example.service;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.model.FamilyDetails;

import java.util.List;
import java.util.Optional;

public interface FamilyDetailsService {

    FamilyDetails create(FamilyDetailsRequestDto dto);

    FamilyDetails update(Long id, FamilyDetails familyDetails);
    FamilyDetails update(Long id, FamilyDetailsRequestDto dto);

    void delete(Long id);

    Optional<FamilyDetails> getById(Long id);
    List<FamilyDetails> getAll();

    Optional<FamilyDetails> getByProfile(Long profileId);
    boolean existsByProfile(Long profileId);

    List<FamilyDetails> getByFamilyType(Long familyTypeId);
    List<FamilyDetails> getByFamily(Long familyId);
    List<FamilyDetails> getActiveByFamily(Long familyId);

    List<FamilyDetails> getByBrotherType(Long brotherTypeId);
    List<FamilyDetails> getBySisterType(Long sisterTypeId);

    List<FamilyDetails> getActiveByProfile(Long profileId);
}