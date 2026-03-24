package com.example.serviceimpl;

import com.example.model.FamilyDetails;
import com.example.repository.FamilyDetailsRepository;
import com.example.service.FamilyDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

    private final FamilyDetailsRepository familyDetailsRepository;

    public FamilyDetailsServiceImpl(FamilyDetailsRepository familyDetailsRepository) {
        this.familyDetailsRepository = familyDetailsRepository;
    }

    // ✅ Create
    @Override
    public FamilyDetails create(FamilyDetails familyDetails) {

        // 🔥 One-to-one validation (Profile should have only one record)
        if (familyDetailsRepository.existsByProfile_Id(familyDetails.getProfile().getId())) {
            throw new RuntimeException("FamilyDetails already exists for this profile");
        }

        return familyDetailsRepository.save(familyDetails);
    }

    // 🔄 Update
    @Override
    public FamilyDetails update(Long id, FamilyDetails familyDetails) {

        FamilyDetails existing = familyDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyDetails not found with id: " + id));

        // ✏️ Update fields (adjust based on your model)
        existing.setFamilyType(familyDetails.getFamilyType());
        existing.setFamily(familyDetails.getFamily());
        existing.setBrotherType(familyDetails.getBrotherType());
        existing.setSisterType(familyDetails.getSisterType());
        existing.setIsActive(familyDetails.getIsActive());

        return familyDetailsRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        FamilyDetails existing = familyDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyDetails not found with id: " + id));

        familyDetailsRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<FamilyDetails> getById(Long id) {
        return familyDetailsRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<FamilyDetails> getAll() {
        return familyDetailsRepository.findAll();
    }

    // 🔍 Profile-based
    @Override
    public Optional<FamilyDetails> getByProfile(Long profileId) {
        return familyDetailsRepository.findByProfile_Id(profileId);
    }

    @Override
    public boolean existsByProfile(Long profileId) {
        return familyDetailsRepository.existsByProfile_Id(profileId);
    }

    // 🔍 FamilyType
    @Override
    public List<FamilyDetails> getByFamilyType(Long familyTypeId) {
        return familyDetailsRepository.findByFamilyType_Id(familyTypeId);
    }

    // 🔍 Family
    @Override
    public List<FamilyDetails> getByFamily(Long familyId) {
        return familyDetailsRepository.findByFamily_Id(familyId);
    }

    @Override
    public List<FamilyDetails> getActiveByFamily(Long familyId) {
        return familyDetailsRepository.findByFamily_IdAndFamily_IsActiveTrue(familyId);
    }

    // 🔍 Brother / Sister
    @Override
    public List<FamilyDetails> getByBrotherType(Long brotherTypeId) {
        return familyDetailsRepository.findByBrotherType_Id(brotherTypeId);
    }

    @Override
    public List<FamilyDetails> getBySisterType(Long sisterTypeId) {
        return familyDetailsRepository.findBySisterType_Id(sisterTypeId);
    }

    // 🔍 Profile Active
    @Override
    public List<FamilyDetails> getActiveByProfile(Long profileId) {
        return familyDetailsRepository.findByProfile_IdAndIsActiveTrue(profileId);
    }
}