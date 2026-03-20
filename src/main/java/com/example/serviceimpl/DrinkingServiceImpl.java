package com.example.serviceimpl;

import com.example.model.Drinking;
import com.example.repository.DrinkingRepository;
import com.example.service.DrinkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DrinkingServiceImpl implements DrinkingService {

    @Autowired
    private DrinkingRepository repo;

    // ✅ Get all
    @Override
    public List<Drinking> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<Drinking> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public Drinking getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Drinking not found with id: " + id));
    }

    // ✅ Create
    @Override
    public Drinking create(Drinking drinking) {
        drinking.setCreatedAt(LocalDateTime.now());
        drinking.setUpdatedAt(LocalDateTime.now());
        drinking.setIsActive(true);
        return repo.save(drinking);
    }

    // ✅ Update
    @Override
    public Drinking update(Long id, Drinking updated) {
        Drinking existing = getById(id);

        existing.setName(updated.getName());
        existing.setValue(updated.getValue());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        Drinking existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Get by admin
    @Override
    public List<Drinking> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Drinking> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search
    @Override
    public List<Drinking> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}