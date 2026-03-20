package com.example.serviceimpl;

import com.example.model.BloodGroup;
import com.example.repository.BloodGroupRepository;
import com.example.service.BloodGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodGroupServiceImpl implements BloodGroupService {

    @Autowired
    private BloodGroupRepository repo;

    // ✅ Get all
    @Override
    public List<BloodGroup> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<BloodGroup> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public BloodGroup getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood group not found"));
    }

    // ✅ Create
    @Override
    public BloodGroup create(BloodGroup bg) {

        if (repo.existsByType(bg.getType())) {
            throw new RuntimeException("Blood group already exists");
        }

        bg.setisActive(true);

        return repo.save(bg);
    }

    // ✅ Update
    @Override
    public BloodGroup update(Long id, BloodGroup updatedBg) {

        BloodGroup existing = getById(id);

        if (!existing.getType().equals(updatedBg.getType())
                && repo.existsByType(updatedBg.getType())) {
            throw new RuntimeException("Blood group already exists");
        }

        existing.setType(updatedBg.getType());
        existing.setisActive(updatedBg.getisActive());
        existing.setAdmin(updatedBg.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    @Override
    public List<BloodGroup> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<BloodGroup> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}