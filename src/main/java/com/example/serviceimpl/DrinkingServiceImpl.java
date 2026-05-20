package com.example.serviceimpl;

import com.example.model.Drinking;
import com.example.repository.DrinkingRepository;
import com.example.service.DrinkingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkingServiceImpl implements DrinkingService {

    private final DrinkingRepository drinkingRepository;

    public DrinkingServiceImpl(DrinkingRepository drinkingRepository) {
        this.drinkingRepository = drinkingRepository;
    }

    // ✅ Create
    @Override
    public Drinking create(Drinking drinking) {

        if (drinkingRepository.existsByNameIgnoreCase(drinking.getName())) {
            throw new RuntimeException("Drinking already exists: " + drinking.getName());
        }

        return drinkingRepository.save(drinking);
    }

    // 🔄 Update
    @Override
    public Drinking update(Long id, Drinking drinking) {

        Drinking existing = drinkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drinking not found with id: " + id));

        drinkingRepository.findByNameIgnoreCase(drinking.getName())
                .ifPresent(d -> {
                    if (!d.getId().equals(id)) {
                        throw new RuntimeException("Drinking already exists: " + drinking.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(drinking.getName());
        existing.setIsActive(drinking.getIsActive());

        return drinkingRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Drinking existing = drinkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drinking not found with id: " + id));

        drinkingRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Drinking> getById(Long id) {
        return drinkingRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Drinking> getAll() {
        return drinkingRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<Drinking> getByName(String name) {
        return drinkingRepository.findByName(name);
    }

    @Override
    public Optional<Drinking> getByNameIgnoreCase(String name) {
        return drinkingRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return drinkingRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return drinkingRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Drinking> getActive() {
        return drinkingRepository.findByIsActiveTrue();
    }

    @Override
    public List<Drinking> getInactive() {
        return drinkingRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Drinking> getByAdmin(Long adminId) {
        return drinkingRepository.findByAdminId(adminId);
    }

    @Override
    public List<Drinking> getActiveByAdmin(Long adminId) {
        return drinkingRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Drinking> search(String keyword) {
        return drinkingRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Drinking> searchByAdmin(Long adminId, String keyword) {
        return drinkingRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}