package com.example.serviceimpl;

import com.example.model.MaritalStatus;
import com.example.repository.MaritalStatusRepository;
import com.example.service.MaritalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaritalStatusServiceImpl implements MaritalStatusService {

    @Autowired
    private MaritalStatusRepository repo;

    // ✅ Save
    @Override
    public MaritalStatus save(MaritalStatus maritalStatus) {
        maritalStatus.setCreatedAt(LocalDateTime.now());
        maritalStatus.setUpdatedAt(LocalDateTime.now());
        maritalStatus.setIsActive(true);
        return repo.save(maritalStatus);
    }

    // ✅ Get by ID
    @Override
    public Optional<MaritalStatus> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Name
    @Override
    public Optional<MaritalStatus> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // ✅ Get all
    @Override
    public List<MaritalStatus> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<MaritalStatus> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<MaritalStatus> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by admin
    @Override
    public List<MaritalStatus> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<MaritalStatus> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search by name
    @Override
    public List<MaritalStatus> searchByName(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Update
    @Override
    public MaritalStatus update(Long id, MaritalStatus updated) {
        MaritalStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("MaritalStatus not found with id: " + id));

        existing.setName(updated.getName());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft Delete
    @Override
    public void delete(Long id) {
        MaritalStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("MaritalStatus not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}