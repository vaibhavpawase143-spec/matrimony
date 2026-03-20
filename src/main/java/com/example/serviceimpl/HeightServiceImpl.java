package com.example.serviceimpl;

import com.example.model.Height;
import com.example.repository.HeightRepository;
import com.example.service.HeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HeightServiceImpl implements HeightService {

    @Autowired
    private HeightRepository repo;

    // ✅ Save
    @Override
    public Height save(Height height) {
        height.setCreatedAt(LocalDateTime.now());
        height.setUpdatedAt(LocalDateTime.now());
        height.setIsActive(true);
        return repo.save(height);
    }

    // ✅ Get by ID
    @Override
    public Optional<Height> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Height (exact match)
    @Override
    public Optional<Height> getByHeight(String height) {
        return repo.findByHeightIgnoreCase(height);
    }

    // ✅ Get all
    @Override
    public List<Height> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<Height> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<Height> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by admin
    @Override
    public List<Height> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Height> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search by height
    @Override
    public List<Height> searchByHeight(String keyword) {
        return repo.findByHeightContainingIgnoreCase(keyword);
    }

    // ✅ Update
    @Override
    public Height update(Long id, Height updated) {
        Height existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Height not found with id: " + id));

        existing.setHeight(updated.getHeight());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft Delete
    @Override
    public void delete(Long id) {
        Height existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Height not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}