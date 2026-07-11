package com.example.serviceimpl;

import com.example.model.FamilyType;
import com.example.repository.FamilyTypeRepository;
import com.example.service.FamilyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyTypeServiceImpl implements FamilyTypeService {

    private final FamilyTypeRepository familyTypeRepository;

    public FamilyTypeServiceImpl(FamilyTypeRepository familyTypeRepository) {
        this.familyTypeRepository = familyTypeRepository;
    }

    // ✅ Create
    @Override
    public FamilyType create(FamilyType familyType) {

        if (familyTypeRepository.existsByNameIgnoreCase(familyType.getName())) {
            throw new RuntimeException("FamilyType already exists: " + familyType.getName());
        }

        return familyTypeRepository.save(familyType);
    }

    // 🔄 Update
    @Override
    public FamilyType update(Long id, FamilyType familyType) {

        FamilyType existing = familyTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyType not found with id: " + id));

        familyTypeRepository.findByNameIgnoreCase(familyType.getName())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("FamilyType already exists: " + familyType.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(familyType.getName());
        existing.setIsActive(familyType.getIsActive());

        return familyTypeRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        FamilyType existing = familyTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyType not found with id: " + id));

        familyTypeRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<FamilyType> getById(Long id) {
        return familyTypeRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<FamilyType> getAll() {
        return familyTypeRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<FamilyType> getByName(String name) {
        return familyTypeRepository.findByName(name);
    }

    @Override
    public Optional<FamilyType> getByNameIgnoreCase(String name) {
        return familyTypeRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return familyTypeRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return familyTypeRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<FamilyType> getActive() {
        return familyTypeRepository.findByIsActiveTrue();
    }

    @Override
    public List<FamilyType> getInactive() {
        return familyTypeRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<FamilyType> getByAdmin(Long adminId) {
        return familyTypeRepository.findByAdminId(adminId);
    }

    @Override
    public List<FamilyType> getActiveByAdmin(Long adminId) {
        return familyTypeRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<FamilyType> search(String keyword) {
        return familyTypeRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<FamilyType> searchByAdmin(Long adminId, String keyword) {
        return familyTypeRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}