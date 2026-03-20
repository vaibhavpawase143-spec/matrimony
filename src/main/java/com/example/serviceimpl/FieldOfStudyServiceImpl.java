package com.example.serviceimpl;

import com.example.model.FieldOfStudy;
import com.example.repository.FieldOfStudyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FieldOfStudyServiceImpl {

    private final FieldOfStudyRepository repo;

    // ✅ Constructor Injection (Best Practice)
    public FieldOfStudyServiceImpl(FieldOfStudyRepository repo) {
        this.repo = repo;
    }

    // ✅ Get all records
    public List<FieldOfStudy> getAll() {
        return repo.findAll();
    }

    // ✅ Get active records
    public List<FieldOfStudy> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get inactive records
    public List<FieldOfStudy> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    public Optional<FieldOfStudy> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Name (FIXED METHOD)
    public Optional<FieldOfStudy> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // ✅ Search by keyword
    public List<FieldOfStudy> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by Admin ID
    public List<FieldOfStudy> getByAdminId(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by Admin ID
    public List<FieldOfStudy> getActiveByAdminId(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Create new record
    public FieldOfStudy create(FieldOfStudy fieldOfStudy) {

        // 🔒 Duplicate check
        if (repo.existsByNameIgnoreCase(fieldOfStudy.getName())) {
            throw new RuntimeException("Field of Study already exists!");
        }

        fieldOfStudy.setIsActive(true);
        fieldOfStudy.setCreatedAt(LocalDateTime.now());

        return repo.save(fieldOfStudy);
    }

    // ✅ Update record
    public FieldOfStudy update(Long id, FieldOfStudy updatedData) {

        FieldOfStudy existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldOfStudy not found with id: " + id));

        // 🔄 Update fields
        existing.setName(updatedData.getName());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft delete (set inactive)
    public void delete(Long id) {
        FieldOfStudy existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldOfStudy not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}