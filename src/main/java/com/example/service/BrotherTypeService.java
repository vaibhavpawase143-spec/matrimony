package com.example.service;

import com.example.model.BrotherType;
import com.example.repository.BrotherTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrotherTypeService {

    @Autowired
    private BrotherTypeRepository repo;

    // ✅ Get all
    public List<BrotherType> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<BrotherType> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public BrotherType getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Brother type not found"));
    }

    // ✅ Create
    public BrotherType create(BrotherType bt) {

        if (repo.existsByValue(bt.getValue())) {
            throw new RuntimeException("Brother type already exists");
        }

        bt.setStatus(true);

        return repo.save(bt);
    }

    // ✅ Update
    public BrotherType update(Long id, BrotherType updated) {

        BrotherType existing = getById(id);

        if (!existing.getValue().equals(updated.getValue())
                && repo.existsByValue(updated.getValue())) {
            throw new RuntimeException("Brother type already exists");
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
    public List<BrotherType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<BrotherType> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }
}