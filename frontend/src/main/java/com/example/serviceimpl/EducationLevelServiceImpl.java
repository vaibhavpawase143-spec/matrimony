package com.example.serviceimpl;

import com.example.model.EducationLevel;
import com.example.repository.EducationLevelRepository;
import com.example.service.EducationLevelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationLevelServiceImpl implements EducationLevelService {

    private final EducationLevelRepository educationLevelRepository;

    public EducationLevelServiceImpl(EducationLevelRepository educationLevelRepository) {
        this.educationLevelRepository = educationLevelRepository;
    }

    // ✅ Create
    @Override
    public EducationLevel create(EducationLevel educationLevel) {

        if (educationLevelRepository.existsByNameIgnoreCase(educationLevel.getName())) {
            throw new RuntimeException("Education level already exists: " + educationLevel.getName());
        }

        return educationLevelRepository.save(educationLevel);
    }

    // 🔄 Update
    @Override
    public EducationLevel update(Long id, EducationLevel educationLevel) {

        EducationLevel existing = educationLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education level not found with id: " + id));

        educationLevelRepository.findByNameIgnoreCase(educationLevel.getName())
                .ifPresent(e -> {
                    if (!e.getId().equals(id)) {
                        throw new RuntimeException("Education level already exists: " + educationLevel.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(educationLevel.getName());
        existing.setIsActive(educationLevel.getIsActive());

        return educationLevelRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        EducationLevel existing = educationLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education level not found with id: " + id));

        educationLevelRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<EducationLevel> getById(Long id) {
        return educationLevelRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<EducationLevel> getAll() {
        return educationLevelRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<EducationLevel> getByName(String name) {
        return educationLevelRepository.findByName(name);
    }

    @Override
    public Optional<EducationLevel> getByNameIgnoreCase(String name) {
        return educationLevelRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return educationLevelRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return educationLevelRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<EducationLevel> getActive() {
        return educationLevelRepository.findByIsActiveTrue();
    }

    @Override
    public List<EducationLevel> getInactive() {
        return educationLevelRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<EducationLevel> getByAdmin(Long adminId) {
        return educationLevelRepository.findByAdminId(adminId);
    }

    @Override
    public List<EducationLevel> getActiveByAdmin(Long adminId) {
        return educationLevelRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<EducationLevel> search(String keyword) {
        return educationLevelRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<EducationLevel> searchByAdmin(Long adminId, String keyword) {
        return educationLevelRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}