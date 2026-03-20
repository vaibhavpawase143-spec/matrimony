package com.example.serviceimpl;

import com.example.model.FamilyType;
import com.example.repository.FamilyTypeRepository;
import com.example.service.FamilyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FamilyTypeServiceImpl implements FamilyTypeService {

    @Autowired
    private FamilyTypeRepository repo;

    // ✅ Save
    @Override
    public FamilyType save(FamilyType familyType) {
        familyType.setCreatedAt(LocalDateTime.now());
        familyType.setUpdatedAt(LocalDateTime.now());
        familyType.setisActive(true);
        return repo.save(familyType);
    }

    // ✅ Get by ID
    @Override
    public Optional<FamilyType> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Name
    @Override
    public Optional<FamilyType> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // ✅ Get all
    @Override
    public List<FamilyType> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<FamilyType> getAllActive() {
        return repo.findByIsActiveTrue();   // ✅ fixed
    }

    // ✅ Get all inactive
    @Override
    public List<FamilyType> getAllInactive() {
        return repo.findByIsActiveFalse();  // ✅ fixed
    }

    // ✅ Get by admin
    @Override
    public List<FamilyType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<FamilyType> getActiveByAdmin(Long adminId) {
       return repo.findByAdminIdAndIsActiveTrue(adminId);  // ✅ fixed
    }

    // ✅ Search by name
    @Override
    public List<FamilyType> searchByName(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Update
    @Override
    public FamilyType update(Long id, FamilyType updated) {
        FamilyType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyType not found with id: " + id));

        existing.setName(updated.getName());
        existing.setisActive(updated.getisActive());   // ✅ fixed
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft Delete
    @Override
    public void delete(Long id) {
        FamilyType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyType not found with id: " + id));

        existing.setisActive(false);   // ✅ fixed
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}