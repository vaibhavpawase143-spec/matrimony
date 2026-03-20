package com.example.serviceimpl;

import com.example.model.Smoking;
import com.example.repository.SmokingRepository;
import com.example.service.SmokingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SmokingServiceImpl implements SmokingService {

    @Autowired
    private SmokingRepository repo;

    // ✅ Create
    @Override
    public Smoking create(Smoking smoking) {
        smoking.setCreatedAt(LocalDateTime.now());
        smoking.setUpdatedAt(LocalDateTime.now());
        smoking.setIsActive(true);
        return repo.save(smoking);
    }

    // ✅ Get all
    @Override
    public List<Smoking> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<Smoking> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<Smoking> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public Optional<Smoking> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public Smoking update(Long id, Smoking smoking) {
        Smoking existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Smoking not found with id: " + id));

        existing.setValue(smoking.getValue());        // ✅ use value instead of name
        existing.setIsActive(smoking.getIsActive());  // ✅ consistent with entity
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        Smoking existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Smoking not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<Smoking> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword); // ✅ use value instead of name
    }

    // ✅ Get by admin
    @Override
    public List<Smoking> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Smoking> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}