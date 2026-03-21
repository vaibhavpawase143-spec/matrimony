package com.example.service;

import com.example.model.FamilyStatus;
import com.example.repository.FamilyStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyStatusService {

    @Autowired
    private FamilyStatusRepository repository;

    // Create
    public FamilyStatus save(FamilyStatus familyStatus) {
        return repository.save(familyStatus);
    }

    // Get by ID
    public Optional<FamilyStatus> getById(Long id) {
        return repository.findById(id);
    }

    // Get by name
    public Optional<FamilyStatus> getByName(String name) {
        return repository.findByName(name);
    }

    // Get all
    public List<FamilyStatus> getAll() {
        return repository.findAll();
    }

    // Get all active
    public List<FamilyStatus> getAllActive() {
        return repository.findByActiveTrue();
    }

    // Get all inactive
    public List<FamilyStatus> getAllInactive() {
        return repository.findByActiveFalse();
    }

    // Get by admin
    public List<FamilyStatus> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // Get active by admin
    public List<FamilyStatus> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndActiveTrue(adminId);
    }

    // Search by name (LIKE)
    public List<FamilyStatus> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    // Update
    public FamilyStatus update(Long id, FamilyStatus updated) {
        FamilyStatus existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found"));

        existing.setName(updated.getName());
        existing.setActive(updated.getActive());
        existing.setAdminId(updated.getAdminId());

        return repository.save(existing);
    }

    // Soft delete
    public void delete(Long id) {
        FamilyStatus existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found"));

        existing.setActive(false); // soft delete
        repository.save(existing);
    }
}