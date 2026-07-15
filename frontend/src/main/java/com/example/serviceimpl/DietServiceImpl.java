package com.example.serviceimpl;

import com.example.model.Diet;
import com.example.repository.DietRepository;
import com.example.service.DietService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;

    public DietServiceImpl(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    // ✅ Create
    @Override
    public Diet create(Diet diet) {

        // 🔥 Duplicate check
        if (dietRepository.existsByNameIgnoreCase(diet.getName())) {
            throw new RuntimeException("Diet already exists: " + diet.getName());
        }

        return dietRepository.save(diet);
    }

    // 🔄 Update
    @Override
    public Diet update(Long id, Diet diet) {

        Diet existing = dietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diet not found with id: " + id));

        // 🔥 Duplicate check (exclude same record)
        dietRepository.findByNameIgnoreCase(diet.getName())
                .ifPresent(d -> {
                    if (!d.getId().equals(id)) {
                        throw new RuntimeException("Diet already exists: " + diet.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(diet.getName());
        existing.setIsActive(diet.getIsActive());

        return dietRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Diet existing = dietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diet not found with id: " + id));

        dietRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Diet> getById(Long id) {
        return dietRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Diet> getAll() {
        return dietRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<Diet> getByName(String name) {
        return dietRepository.findByName(name);
    }

    @Override
    public Optional<Diet> getByNameIgnoreCase(String name) {
        return dietRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return dietRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return dietRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Diet> getActive() {
        return dietRepository.findByIsActiveTrue();
    }

    @Override
    public List<Diet> getInactive() {
        return dietRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Diet> getByAdmin(Long adminId) {
        return dietRepository.findByAdminId(adminId);
    }

    @Override
    public List<Diet> getActiveByAdmin(Long adminId) {
        return dietRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Diet> search(String keyword) {
        return dietRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Diet> searchByAdmin(Long adminId, String keyword) {
        return dietRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}