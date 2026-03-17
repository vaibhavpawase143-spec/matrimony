package com.example.service;

import com.example.model.BloodGroup;
import com.example.repository.BloodGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodGroupService {

    @Autowired
    private BloodGroupRepository repo;

    // ✅ Get all blood groups
    public List<BloodGroup> getAll() {
        return repo.findAll();
    }

    // ✅ Get only active blood groups
    public List<BloodGroup> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public BloodGroup getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood group not found"));
    }

    // ✅ Create blood group (with duplicate check)
    public BloodGroup create(BloodGroup bg) {

        if (repo.existsByType(bg.getType())) {
            throw new RuntimeException("Blood group already exists");
        }

        bg.setStatus(true); // default active

        return repo.save(bg);
    }

    // ✅ Update blood group
    public BloodGroup update(Long id, BloodGroup updatedBg) {

        BloodGroup existing = getById(id);

        // If type changed → check duplicate
        if (!existing.getType().equals(updatedBg.getType())
                && repo.existsByType(updatedBg.getType())) {
            throw new RuntimeException("Blood group already exists");
        }

        existing.setType(updatedBg.getType());
        existing.setStatus(updatedBg.getStatus());
        existing.setAdmin(updatedBg.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete blood group
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<BloodGroup> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active blood groups by admin
    public List<BloodGroup> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }
}