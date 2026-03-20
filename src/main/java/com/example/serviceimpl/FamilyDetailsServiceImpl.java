package com.example.serviceimpl;

import com.example.model.FamilyDetails;
import com.example.repository.FamilyDetailsRepository;
import com.example.service.FamilyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

    @Autowired
    private FamilyDetailsRepository repo;

    // ✅ Save
    @Override
    public FamilyDetails saveFamilyDetails(FamilyDetails familyDetails) {
        return repo.save(familyDetails);
    }

    // ✅ Get by profileId
    @Override
    public Optional<FamilyDetails> getByProfileId(Long profileId) {
        return repo.findByProfile_Id(profileId);
    }

    // ✅ Exists by profileId
    @Override
    public boolean existsByProfileId(Long profileId) {
        return repo.existsByProfile_Id(profileId);
    }

    // ✅ Get by family type
    @Override
    public List<FamilyDetails> getByFamilyType(Long familyTypeId) {
        return repo.findByFamilyType_Id(familyTypeId);
    }

    // ✅ Get by family isActive
    @Override
    public List<FamilyDetails> getByFamilyStatus(Long FamilyStatusId) {
        return repo.findByFamily_IsActiveTrue();
    }

    // ✅ Get by brother type
    @Override
    public List<FamilyDetails> getByBrotherType(Long brotherTypeId) {
        return repo.findByBrotherType_Id(brotherTypeId);
    }

    // ✅ Get by sister type
    @Override
    public List<FamilyDetails> getBySisterType(Long sisterTypeId) {
        return repo.findBySisterType_Id(sisterTypeId);
    }

    // ✅ Update by profileId
    @Override
    public FamilyDetails updateFamilyDetails(Long profileId, FamilyDetails updatedDetails) {
        FamilyDetails existing = repo.findByProfile_Id(profileId)
                .orElseThrow(() -> new RuntimeException("FamilyDetails not found for profileId: " + profileId));

        // Update relationships directly

        existing.setFamilyType(updatedDetails.getFamilyType());
        existing.setBrotherType(updatedDetails.getBrotherType());
        existing.setSisterType(updatedDetails.getSisterType());

        // Update simple fields
        existing.setFatherOccupation(updatedDetails.getFatherOccupation());
        existing.setMotherOccupation(updatedDetails.getMotherOccupation());

        return repo.save(existing);
    }

    // ✅ Delete by ID
    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}