package com.example.serviceimpl;

import com.example.model.Occupation;
import com.example.repository.OccupationRepository;
import com.example.service.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OccupationServiceImpl implements OccupationService {

    @Autowired
    private OccupationRepository repo;

    // ✅ Create
    @Override
    public Occupation create(Occupation occupation) {
        occupation.setCreatedAt(LocalDateTime.now());
        occupation.setUpdatedAt(LocalDateTime.now());
        occupation.setIsActive(true);
        return repo.save(occupation);
    }

    // ✅ Get all
    @Override
    public List<Occupation> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<Occupation> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<Occupation> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public Optional<Occupation> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public Occupation update(Long id, Occupation occupation) {
        Occupation existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Occupation not found with id: " + id));

        existing.setName(occupation.getName());
        existing.setIsActive(occupation.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        Occupation existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Occupation not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<Occupation> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by admin
    @Override
    public List<Occupation> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Occupation> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}