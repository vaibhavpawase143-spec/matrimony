package com.example.serviceimpl;

import com.example.model.FamilyValue;
import com.example.repository.FamilyValueRepository;
import com.example.service.FamilyValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyValueServiceImpl implements FamilyValueService {

    private final FamilyValueRepository familyValueRepository;

    public FamilyValueServiceImpl(FamilyValueRepository familyValueRepository) {
        this.familyValueRepository = familyValueRepository;
    }

    // ✅ Create
    @Override
    public FamilyValue create(FamilyValue familyValue) {

        if (familyValueRepository.existsByNameIgnoreCase(familyValue.getName())) {
            throw new RuntimeException("FamilyValue already exists: " + familyValue.getName());
        }

        return familyValueRepository.save(familyValue);
    }

    // 🔄 Update
    @Override
    public FamilyValue update(Long id, FamilyValue familyValue) {

        FamilyValue existing = familyValueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyValue not found with id: " + id));

        familyValueRepository.findByNameIgnoreCase(familyValue.getName())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("FamilyValue already exists: " + familyValue.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(familyValue.getName());
        existing.setIsActive(familyValue.getIsActive());

        return familyValueRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        FamilyValue existing = familyValueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyValue not found with id: " + id));

        familyValueRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<FamilyValue> getById(Long id) {
        return familyValueRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<FamilyValue> getAll() {
        return familyValueRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<FamilyValue> getByName(String name) {
        return familyValueRepository.findByName(name);
    }

    @Override
    public Optional<FamilyValue> getByNameIgnoreCase(String name) {
        return familyValueRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return familyValueRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return familyValueRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<FamilyValue> getActive() {
        return familyValueRepository.findByIsActiveTrue();
    }

    @Override
    public List<FamilyValue> getInactive() {
        return familyValueRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<FamilyValue> getByAdmin(Long adminId) {
        return familyValueRepository.findByAdminId(adminId);
    }

    @Override
    public List<FamilyValue> getActiveByAdmin(Long adminId) {
        return familyValueRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<FamilyValue> search(String keyword) {
        return familyValueRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<FamilyValue> searchByAdmin(Long adminId, String keyword) {
        return familyValueRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}