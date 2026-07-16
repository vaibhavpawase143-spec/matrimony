package com.example.serviceimpl;

import com.example.model.FamilyStatus;
import com.example.repository.FamilyStatusRepository;
import com.example.service.FamilyStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyStatusServiceImpl implements FamilyStatusService {

    private final FamilyStatusRepository familyStatusRepository;

    public FamilyStatusServiceImpl(FamilyStatusRepository familyStatusRepository) {
        this.familyStatusRepository = familyStatusRepository;
    }

    // ✅ Create
    @Override
    public FamilyStatus create(FamilyStatus familyStatus) {

        if (familyStatusRepository.existsByNameIgnoreCase(familyStatus.getName())) {
            throw new RuntimeException("FamilyStatus already exists: " + familyStatus.getName());
        }

        return familyStatusRepository.save(familyStatus);
    }

    // 🔄 Update
    @Override
    public FamilyStatus update(Long id, FamilyStatus familyStatus) {

        FamilyStatus existing = familyStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found with id: " + id));

        familyStatusRepository.findByNameIgnoreCase(familyStatus.getName())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("FamilyStatus already exists: " + familyStatus.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(familyStatus.getName());
        existing.setIsActive(familyStatus.getIsActive());

        return familyStatusRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        FamilyStatus existing = familyStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found with id: " + id));

        familyStatusRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<FamilyStatus> getById(Long id) {
        return familyStatusRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<FamilyStatus> getAll() {
        return familyStatusRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<FamilyStatus> getByName(String name) {
        return familyStatusRepository.findByName(name);
    }

    @Override
    public Optional<FamilyStatus> getByNameIgnoreCase(String name) {
        return familyStatusRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return familyStatusRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return familyStatusRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<FamilyStatus> getActive() {
        return familyStatusRepository.findByIsActiveTrue();
    }

    @Override
    public List<FamilyStatus> getInactive() {
        return familyStatusRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<FamilyStatus> getByAdmin(Long adminId) {
        return familyStatusRepository.findByAdminId(adminId);
    }

    @Override
    public List<FamilyStatus> getActiveByAdmin(Long adminId) {
        return familyStatusRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<FamilyStatus> search(String keyword) {
        return familyStatusRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<FamilyStatus> searchByAdmin(Long adminId, String keyword) {
        return familyStatusRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}