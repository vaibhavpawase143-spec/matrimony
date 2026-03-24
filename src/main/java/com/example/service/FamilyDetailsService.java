package com.example.service;

import com.example.model.FamilyDetails;

import java.util.List;
import java.util.Optional;

public interface FamilyDetailsService {

    // 🔍 Basic CRUD
    FamilyDetails create(FamilyDetails familyDetails);

    FamilyDetails update(Long id, FamilyDetails familyDetails);

    void delete(Long id);

    Optional<FamilyDetails> getById(Long id);

    List<FamilyDetails> getAll();

    // 🔍 Profile-based (One-to-One)
    Optional<FamilyDetails> getByProfile(Long profileId);

    boolean existsByProfile(Long profileId);

    // 🔍 FamilyType
    List<FamilyDetails> getByFamilyType(Long familyTypeId);

    // 🔍 Family
    List<FamilyDetails> getByFamily(Long familyId);

    List<FamilyDetails> getActiveByFamily(Long familyId);

    // 🔍 Brother / Sister
    List<FamilyDetails> getByBrotherType(Long brotherTypeId);

    List<FamilyDetails> getBySisterType(Long sisterTypeId);

    // 🔍 Profile Active
    List<FamilyDetails> getActiveByProfile(Long profileId);


}