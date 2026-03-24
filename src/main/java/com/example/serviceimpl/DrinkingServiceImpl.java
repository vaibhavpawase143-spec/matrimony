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

        if (drinkingRepository.existsByValueIgnoreCase(drinking.getValue())) {
            throw new RuntimeException("Drinking already exists: " + drinking.getValue());
        }

        return drinkingRepository.save(drinking);
    }

    // 🔄 Update
    @Override
    public Drinking update(Long id, Drinking drinking) {

        Drinking existing = drinkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drinking not found with id: " + id));

        drinkingRepository.findByValueIgnoreCase(drinking.getValue())
                .ifPresent(d -> {
                    if (!d.getId().equals(id)) {
                        throw new RuntimeException("Drinking already exists: " + drinking.getValue());
                    }
                });

        // ✏️ Update fields
        existing.setValue(drinking.getValue());
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

    // 🔍 Find by value
    @Override
    public Optional<Drinking> getByValue(String value) {
        return drinkingRepository.findByValue(value);
    }

    @Override
    public Optional<Drinking> getByValueIgnoreCase(String value) {
        return drinkingRepository.findByValueIgnoreCase(value);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByValue(String value) {
        return drinkingRepository.existsByValue(value);
    }

    @Override
    public boolean existsByValueIgnoreCase(String value) {
        return drinkingRepository.existsByValueIgnoreCase(value);
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
        return drinkingRepository.findByValueContainingIgnoreCase(keyword);
    }

    @Override
    public List<Drinking> searchByAdmin(Long adminId, String keyword) {
        return drinkingRepository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }
}