package com.example.serviceimpl;

import com.example.model.ManglikStatus;
import com.example.repository.ManglikStatusRepository;
import com.example.service.ManglikStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ManglikStatusServiceImpl implements ManglikStatusService {

    @Autowired
    private ManglikStatusRepository repo;

    // ✅ Save
    @Override
    public ManglikStatus save(ManglikStatus ManglikStatus) {
        ManglikStatus.setCreatedAt(LocalDateTime.now());
        ManglikStatus.setUpdatedAt(LocalDateTime.now());
        ManglikStatus.setIsActive(true);
        return repo.save(ManglikStatus);
    }

    // ✅ Get by ID
    @Override
    public Optional<ManglikStatus> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Get by Name
    @Override
    public Optional<ManglikStatus> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // ✅ Get all
    @Override
    public List<ManglikStatus> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<ManglikStatus> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<ManglikStatus> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by admin
    @Override
    public List<ManglikStatus> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<ManglikStatus> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search by name
    @Override
    public List<ManglikStatus> searchByName(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Update
    @Override
    public ManglikStatus update(Long id, ManglikStatus updated) {
        ManglikStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ManglikStatus not found with id: " + id));

        existing.setName(updated.getName());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Soft Delete
    @Override
    public void delete(Long id) {
        ManglikStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ManglikStatus not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }
}