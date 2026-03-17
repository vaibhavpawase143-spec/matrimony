package com.example.service;

import com.example.model.DisabilityStatus;
import com.example.repository.DisabilityStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DisabilityStatusService {

    @Autowired
    private DisabilityStatusRepository repo;

    // ✅ Get all
    public List<DisabilityStatus> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<DisabilityStatus> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public DisabilityStatus getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Disability status not found"));
    }

    // ✅ Create
    public DisabilityStatus create(DisabilityStatus ds) {

        if (repo.existsByValue(ds.getValue())) {
            throw new RuntimeException("Disability status already exists");
        }

        ds.setStatus(true);

        return repo.save(ds);
    }

    // ✅ Update
    public DisabilityStatus update(Long id, DisabilityStatus updated) {

        DisabilityStatus existing = getById(id);

        // duplicate check if value changed
        if (!existing.getValue().equals(updated.getValue())
                && repo.existsByValue(updated.getValue())) {
            throw new RuntimeException("Disability status already exists");
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
    public List<DisabilityStatus> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<DisabilityStatus> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<DisabilityStatus> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}