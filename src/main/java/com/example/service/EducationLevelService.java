package com.example.service;

import com.example.model.EducationLevel;
import com.example.repository.EducationLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationLevelService {

    @Autowired
    private EducationLevelRepository repo;

    // ✅ Get all
    public List<EducationLevel> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<EducationLevel> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public EducationLevel getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education level not found"));
    }

    // ✅ Create
    public EducationLevel create(EducationLevel edu) {

        if (repo.existsByName(edu.getName())) {
            throw new RuntimeException("Education level already exists");
        }

        edu.setStatus(true);

        return repo.save(edu);
    }

    // ✅ Update
    public EducationLevel update(Long id, EducationLevel updated) {

        EducationLevel existing = getById(id);

        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("Education level already exists");
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
    public List<EducationLevel> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<EducationLevel> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<EducationLevel> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}