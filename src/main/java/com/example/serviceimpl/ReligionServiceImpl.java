package com.example.serviceimpl;

import com.example.model.Religion;
import com.example.repository.ReligionRepository;
import com.example.service.ReligionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReligionServiceImpl implements ReligionService {

    @Autowired
    private ReligionRepository repo;

    // ✅ Create
    @Override
    public Religion create(Religion religion) {
        religion.setCreatedAt(LocalDateTime.now());
        religion.setUpdatedAt(LocalDateTime.now());
        religion.setIsActive(true);
        return repo.save(religion);
    }

    // ✅ Get all
    @Override
    public List<Religion> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<Religion> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<Religion> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public Optional<Religion> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public Religion update(Long id, Religion religion) {
        Religion existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Religion not found with id: " + id));

        existing.setName(religion.getName());
        existing.setIsActive(religion.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        Religion existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Religion not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<Religion> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by admin
    @Override
    public List<Religion> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Religion> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}