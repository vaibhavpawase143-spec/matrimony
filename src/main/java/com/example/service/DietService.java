package com.example.service;

import com.example.model.Diet;
import com.example.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DietService {

    @Autowired
    private DietRepository repo;

    // ✅ Get all
    public List<Diet> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<Diet> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Diet getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Diet not found"));
    }

    // ✅ Create
    public Diet create(Diet diet) {

        if (repo.existsByName(diet.getName())) {
            throw new RuntimeException("Diet already exists");
        }

        diet.setStatus(true);

        return repo.save(diet);
    }

    // ✅ Update
    public Diet update(Long id, Diet updated) {

        Diet existing = getById(id);

        // duplicate check if name changed
        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("Diet already exists");
        }

        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<Diet> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<Diet> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<Diet> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}