package com.example.serviceimpl;

import com.example.model.ManglikStatus;
import com.example.repository.ManglikStatusRepository;
import com.example.service.ManglikStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManglikStatusServiceImpl implements ManglikStatusService {

    private final ManglikStatusRepository manglikStatusRepository;

    public ManglikStatusServiceImpl(ManglikStatusRepository manglikStatusRepository) {
        this.manglikStatusRepository = manglikStatusRepository;
    }

    // ✅ Create
    @Override
    public ManglikStatus create(ManglikStatus manglikStatus) {

        if (manglikStatusRepository.existsByNameIgnoreCase(manglikStatus.getName())) {
            throw new RuntimeException("ManglikStatus already exists: " + manglikStatus.getName());
        }

        return manglikStatusRepository.save(manglikStatus);
    }

    // 🔄 Update
    @Override
    public ManglikStatus update(Long id, ManglikStatus manglikStatus) {

        ManglikStatus existing = manglikStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ManglikStatus not found with id: " + id));

        manglikStatusRepository.findByNameIgnoreCase(manglikStatus.getName())
                .ifPresent(m -> {
                    if (!m.getId().equals(id)) {
                        throw new RuntimeException("ManglikStatus already exists: " + manglikStatus.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(manglikStatus.getName());
        existing.setIsActive(manglikStatus.getIsActive());

        return manglikStatusRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        ManglikStatus existing = manglikStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ManglikStatus not found with id: " + id));

        manglikStatusRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<ManglikStatus> getById(Long id) {
        return manglikStatusRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<ManglikStatus> getAll() {
        return manglikStatusRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<ManglikStatus> getByName(String name) {
        return manglikStatusRepository.findByName(name);
    }

    @Override
    public Optional<ManglikStatus> getByNameIgnoreCase(String name) {
        return manglikStatusRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return manglikStatusRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return manglikStatusRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<ManglikStatus> getActive() {
        return manglikStatusRepository.findByIsActiveTrue();
    }

    @Override
    public List<ManglikStatus> getInactive() {
        return manglikStatusRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<ManglikStatus> getByAdmin(Long adminId) {
        return manglikStatusRepository.findByAdminId(adminId);
    }

    @Override
    public List<ManglikStatus> getActiveByAdmin(Long adminId) {
        return manglikStatusRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<ManglikStatus> search(String keyword) {
        return manglikStatusRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<ManglikStatus> searchByAdmin(Long adminId, String keyword) {
        return manglikStatusRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}