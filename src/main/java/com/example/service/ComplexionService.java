package com.example.service;

import com.example.model.Complexion;
import com.example.repository.ComplexionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplexionService {

    @Autowired
    private ComplexionRepository repo;

    // ✅ Get all
    public List<Complexion> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<Complexion> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Complexion getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complexion not found"));
    }

    // ✅ Create
    public Complexion create(Complexion complexion) {

        if (repo.existsByValue(complexion.getValue())) {
            throw new RuntimeException("Complexion already exists");
        }

        complexion.setStatus(true);

        return repo.save(complexion);
    }

    // ✅ Update
    public Complexion update(Long id, Complexion updated) {

        Complexion existing = getById(id);

        // duplicate check if value changed
        if (!existing.getValue().equals(updated.getValue())
                && repo.existsByValue(updated.getValue())) {
            throw new RuntimeException("Complexion already exists");
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
    public List<Complexion> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<Complexion> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<Complexion> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}