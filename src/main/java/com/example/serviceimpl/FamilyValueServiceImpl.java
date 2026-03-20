package com.example.serviceimpl;

import com.example.model.FamilyValue;
import com.example.repository.FamilyValueRepository;
import com.example.service.FamilyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FamilyValueServiceImpl implements FamilyValueService {

    @Autowired
    private FamilyValueRepository repo;

    // ✅ Save
    @Override
    public FamilyValue save(FamilyValue familyValue) {
        familyValue.setCreatedAt(LocalDateTime.now());
        familyValue.setUpdatedAt(LocalDateTime.now());
        familyValue.setIsActive(true);
        return repo.save(familyValue);
    }

    // ✅ Get by ID
    @Override
    public Optional<FamilyValue> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Name
    @Override
    public Optional<FamilyValue> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // ✅ Get all
    @Override
    public List<FamilyValue> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<FamilyValue> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<FamilyValue> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by admin
    @Override
    public List<FamilyValue> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<FamilyValue> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search by name
    @Override
    public List<FamilyValue> searchByName(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Update
    @Override
    public FamilyValue update(Long id, FamilyValue updated) {
        FamilyValue existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyValue not found with id: " + id));

        existing.setName(updated.getName());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft Delete
    @Override
    public void delete(Long id) {
        FamilyValue existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyValue not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}