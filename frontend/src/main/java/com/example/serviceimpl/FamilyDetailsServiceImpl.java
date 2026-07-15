package com.example.serviceimpl;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.model.*;
import com.example.repository.FamilyDetailsRepository;
import com.example.service.FamilyDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

    private final FamilyDetailsRepository repository;

    public FamilyDetailsServiceImpl(FamilyDetailsRepository repository) {
        this.repository = repository;
    }

    // ================= CREATE =================
    @Override
    public FamilyDetails create(FamilyDetailsRequestDto dto) {

        if (repository.existsByProfile_Id(dto.getProfileId())) {
            throw new RuntimeException("FamilyDetails already exists for this profile");
        }

        FamilyDetails entity = new FamilyDetails();

        // 🔗 relations
        if (dto.getProfileId() != null) {
            Profile p = new Profile();
            p.setId(dto.getProfileId());
            entity.setProfile(p);
        }

        if (dto.getFamilyTypeId() != null) {
            FamilyType ft = new FamilyType();
            ft.setId(dto.getFamilyTypeId());
            entity.setFamilyType(ft);
        }

        if (dto.getFamilyId() != null) {
            Family f = new Family();
            f.setId(dto.getFamilyId());
            entity.setFamily(f);
        }

        if (dto.getBrotherTypeId() != null) {
            BrotherType bt = new BrotherType();
            bt.setId(dto.getBrotherTypeId());
            entity.setBrotherType(bt);
        }

        if (dto.getSisterTypeId() != null) {
            SisterType st = new SisterType();
            st.setId(dto.getSisterTypeId());
            entity.setSisterType(st);
        }

        // fields
        entity.setFatherOccupation(dto.getFatherOccupation());
        entity.setMotherOccupation(dto.getMotherOccupation());
        entity.setIsActive(true);

        return repository.save(entity);
    }

    // ================= UPDATE DTO =================
    @Override
    public FamilyDetails update(Long id, FamilyDetailsRequestDto dto) {

        FamilyDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (dto.getFamilyTypeId() != null) {
            FamilyType ft = new FamilyType();
            ft.setId(dto.getFamilyTypeId());
            existing.setFamilyType(ft);
        }

        if (dto.getFamilyId() != null) {
            Family f = new Family();
            f.setId(dto.getFamilyId());
            existing.setFamily(f);
        }

        if (dto.getBrotherTypeId() != null) {
            BrotherType bt = new BrotherType();
            bt.setId(dto.getBrotherTypeId());
            existing.setBrotherType(bt);
        }

        if (dto.getSisterTypeId() != null) {
            SisterType st = new SisterType();
            st.setId(dto.getSisterTypeId());
            existing.setSisterType(st);
        }

        if (dto.getFatherOccupation() != null)
            existing.setFatherOccupation(dto.getFatherOccupation());

        if (dto.getMotherOccupation() != null)
            existing.setMotherOccupation(dto.getMotherOccupation());

        return repository.save(existing);
    }

    // ================= UPDATE ENTITY =================
    @Override
    public FamilyDetails update(Long id, FamilyDetails familyDetails) {
        return update(id, mapToDto(familyDetails)); // optional reuse
    }

    private FamilyDetailsRequestDto mapToDto(FamilyDetails f) {
        FamilyDetailsRequestDto dto = new FamilyDetailsRequestDto();
        dto.setFamilyId(f.getFamily() != null ? f.getFamily().getId() : null);
        dto.setFamilyTypeId(f.getFamilyType() != null ? f.getFamilyType().getId() : null);
        dto.setBrotherTypeId(f.getBrotherType() != null ? f.getBrotherType().getId() : null);
        dto.setSisterTypeId(f.getSisterType() != null ? f.getSisterType().getId() : null);
        dto.setFatherOccupation(f.getFatherOccupation());
        dto.setMotherOccupation(f.getMotherOccupation());
        return dto;
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ================= GET =================
    @Override
    public Optional<FamilyDetails> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<FamilyDetails> getAll() {
        return repository.findAll();
    }

    // ================= PROFILE =================
    @Override
    public Optional<FamilyDetails> getByProfile(Long profileId) {
        return repository.findByProfile_Id(profileId);
    }

    @Override
    public boolean existsByProfile(Long profileId) {
        return repository.existsByProfile_Id(profileId);
    }

    // ================= FILTER =================
    @Override
    public List<FamilyDetails> getByFamilyType(Long familyTypeId) {
        return repository.findByFamilyType_Id(familyTypeId);
    }

    @Override
    public List<FamilyDetails> getByFamily(Long familyId) {
        return repository.findByFamily_Id(familyId);
    }

    @Override
    public List<FamilyDetails> getActiveByFamily(Long familyId) {
        return repository.findByFamily_IdAndFamily_IsActiveTrue(familyId);
    }

    @Override
    public List<FamilyDetails> getByBrotherType(Long brotherTypeId) {
        return repository.findByBrotherType_Id(brotherTypeId);
    }

    @Override
    public List<FamilyDetails> getBySisterType(Long sisterTypeId) {
        return repository.findBySisterType_Id(sisterTypeId);
    }

    @Override
    public List<FamilyDetails> getActiveByProfile(Long profileId) {
        return repository.findByProfile_IdAndIsActiveTrue(profileId);
    }
}