package com.example.serviceimpl;

import com.example.model.FieldOfStudy;
import com.example.repository.FieldOfStudyRepository;
import com.example.service.FieldOfStudyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldOfStudyServiceImpl implements FieldOfStudyService {

    private final FieldOfStudyRepository fieldOfStudyRepository;

    public FieldOfStudyServiceImpl(FieldOfStudyRepository fieldOfStudyRepository) {
        this.fieldOfStudyRepository = fieldOfStudyRepository;
    }

    // ✅ Create
    @Override
    public FieldOfStudy create(FieldOfStudy fieldOfStudy) {

        if (fieldOfStudyRepository.existsByNameIgnoreCase(fieldOfStudy.getName())) {
            throw new RuntimeException("FieldOfStudy already exists: " + fieldOfStudy.getName());
        }

        return fieldOfStudyRepository.save(fieldOfStudy);
    }

    // 🔄 Update
    @Override
    public FieldOfStudy update(Long id, FieldOfStudy fieldOfStudy) {

        FieldOfStudy existing = fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldOfStudy not found with id: " + id));

        fieldOfStudyRepository.findByNameIgnoreCase(fieldOfStudy.getName())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("FieldOfStudy already exists: " + fieldOfStudy.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(fieldOfStudy.getName());
        existing.setIsActive(fieldOfStudy.getIsActive());

        return fieldOfStudyRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        FieldOfStudy existing = fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldOfStudy not found with id: " + id));

        fieldOfStudyRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<FieldOfStudy> getById(Long id) {
        return fieldOfStudyRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<FieldOfStudy> getAll() {
        return fieldOfStudyRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<FieldOfStudy> getByName(String name) {
        return fieldOfStudyRepository.findByName(name);
    }

    @Override
    public Optional<FieldOfStudy> getByNameIgnoreCase(String name) {
        return fieldOfStudyRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return fieldOfStudyRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return fieldOfStudyRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<FieldOfStudy> getActive() {
        return fieldOfStudyRepository.findByIsActiveTrue();
    }

    @Override
    public List<FieldOfStudy> getInactive() {
        return fieldOfStudyRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<FieldOfStudy> getByAdmin(Long adminId) {
        return fieldOfStudyRepository.findByAdminId(adminId);
    }

    @Override
    public List<FieldOfStudy> getActiveByAdmin(Long adminId) {
        return fieldOfStudyRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<FieldOfStudy> search(String keyword) {
        return fieldOfStudyRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<FieldOfStudy> searchByAdmin(Long adminId, String keyword) {
        return fieldOfStudyRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}