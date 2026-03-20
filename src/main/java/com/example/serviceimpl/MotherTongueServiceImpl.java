package com.example.serviceimpl;

import com.example.model.MotherTongue;
import com.example.repository.MotherTongueRepository;
import com.example.service.MotherTongueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MotherTongueServiceImpl implements MotherTongueService {

    @Autowired
    private MotherTongueRepository repo;

    // ✅ Create
    @Override
    public MotherTongue create(MotherTongue motherTongue) {
        motherTongue.setCreatedAt(LocalDateTime.now());
        motherTongue.setUpdatedAt(LocalDateTime.now());
        motherTongue.setIsActive(true);
        return repo.save(motherTongue);
    }

    // ✅ Get all
    @Override
    public List<MotherTongue> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<MotherTongue> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<MotherTongue> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public Optional<MotherTongue> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public MotherTongue update(Long id, MotherTongue motherTongue) {
        MotherTongue existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("MotherTongue not found with id: " + id));

        existing.setName(motherTongue.getName());
        existing.setIsActive(motherTongue.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete recommended)
    @Override
    public void delete(Long id) {
        MotherTongue existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("MotherTongue not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<MotherTongue> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by admin
    @Override
    public List<MotherTongue> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<MotherTongue> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}