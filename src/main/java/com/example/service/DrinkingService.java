package com.example.service;

import com.example.model.Drinking;
import com.example.repository.DrinkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DrinkingService {

    @Autowired
    private DrinkingRepository repo;

    // ✅ Get all
    public List<Drinking> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<Drinking> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Drinking getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Drinking type not found"));
    }

    // ✅ Create
    public Drinking create(Drinking drinking) {

        if (repo.existsByValue(drinking.getValue())) {
            throw new RuntimeException("Drinking type already exists");
        }

        drinking.setStatus(true);

        return repo.save(drinking);
    }

    // ✅ Update
    public Drinking update(Long id, Drinking updated) {

        Drinking existing = getById(id);

        // duplicate check if value changed
        if (!existing.getValue().equals(updated.getValue())
                && repo.existsByValue(updated.getValue())) {
            throw new RuntimeException("Drinking type already exists");
        }

        existing.setValue(updated.getValue());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<Drinking> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<Drinking> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<Drinking> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}