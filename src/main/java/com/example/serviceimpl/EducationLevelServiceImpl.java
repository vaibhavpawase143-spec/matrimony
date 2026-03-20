package com.example.serviceimpl;

import com.example.model.EducationLevel;
import com.example.repository.EducationLevelRepository;
import com.example.service.EducationLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EducationLevelServiceImpl implements EducationLevelService {

    @Autowired
    private EducationLevelRepository repo;

    // ✅ Get all
    @Override
    public List<EducationLevel> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<EducationLevel> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public EducationLevel getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("EducationLevel not found with id: " + id));
    }

    // ✅ Create
    @Override
    public EducationLevel create(EducationLevel edu) {
        edu.setCreatedAt(LocalDateTime.now());
        edu.setUpdatedAt(LocalDateTime.now());
        edu.setIsActive(true);
        return repo.save(edu);
    }

    // ✅ Update
    @Override
    public EducationLevel update(Long id, EducationLevel updated) {
        EducationLevel existing = getById(id);

        existing.setName(updated.getName());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        EducationLevel existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Get by admin
    @Override
    public List<EducationLevel> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<EducationLevel> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search
    @Override
    public List<EducationLevel> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}