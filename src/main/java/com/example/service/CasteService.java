package com.example.service;

import com.example.model.Caste;
import com.example.repository.CasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasteService {

    @Autowired
    private CasteRepository repo;

    // ✅ Get all castes
    public List<Caste> getAll() {
        return repo.findAll();
    }

    // ✅ Get active castes
    public List<Caste> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Caste getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caste not found"));
    }

    // ✅ Create caste
    public Caste create(Caste caste) {

        if (repo.existsByName(caste.getName())) {
            throw new RuntimeException("Caste already exists");
        }

        caste.setStatus(true);

        return repo.save(caste);
    }

    // ✅ Update caste
    public Caste update(Long id, Caste updated) {

        Caste existing = getById(id);

        // Check duplicate if name changed
        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("Caste already exists");
        }

        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());
        existing.setReligion(updated.getReligion());

        return repo.save(existing);
    }

    // ✅ Delete caste
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by religion
    public List<Caste> getByReligion(Long religionId) {
        return repo.findByReligionId(religionId);
    }

    // ✅ Get active by religion (important for dropdown)
    public List<Caste> getActiveByReligion(Long religionId) {
        return repo.findByReligionIdAndStatusTrue(religionId);
    }

    // ✅ Get by admin
    public List<Caste> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Search caste
    public List<Caste> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}