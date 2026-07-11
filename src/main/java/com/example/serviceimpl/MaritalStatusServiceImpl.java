package com.example.serviceimpl;

import com.example.model.MaritalStatus;
import com.example.repository.MaritalStatusRepository;
import com.example.service.MaritalStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaritalStatusServiceImpl implements MaritalStatusService {

    private final MaritalStatusRepository maritalStatusRepository;

    public MaritalStatusServiceImpl(MaritalStatusRepository maritalStatusRepository) {
        this.maritalStatusRepository = maritalStatusRepository;
    }

    // ✅ Create
    @Override
    public MaritalStatus create(MaritalStatus maritalStatus) {

        if (maritalStatusRepository.existsByNameIgnoreCase(maritalStatus.getName())) {
            throw new RuntimeException("MaritalStatus already exists: " + maritalStatus.getName());
        }

        return maritalStatusRepository.save(maritalStatus);
    }

    // 🔄 Update
    @Override
    public MaritalStatus update(Long id, MaritalStatus maritalStatus) {

        MaritalStatus existing = maritalStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaritalStatus not found with id: " + id));

        maritalStatusRepository.findByNameIgnoreCase(maritalStatus.getName())
                .ifPresent(m -> {
                    if (!m.getId().equals(id)) {
                        throw new RuntimeException("MaritalStatus already exists: " + maritalStatus.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(maritalStatus.getName());
        existing.setIsActive(maritalStatus.getIsActive());

        return maritalStatusRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        MaritalStatus existing = maritalStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaritalStatus not found with id: " + id));

        maritalStatusRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<MaritalStatus> getById(Long id) {
        return maritalStatusRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<MaritalStatus> getAll() {
        return maritalStatusRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<MaritalStatus> getByName(String name) {
        return maritalStatusRepository.findByName(name);
    }

    @Override
    public Optional<MaritalStatus> getByNameIgnoreCase(String name) {
        return maritalStatusRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return maritalStatusRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return maritalStatusRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<MaritalStatus> getActive() {
        return maritalStatusRepository.findByIsActiveTrue();
    }

    @Override
    public List<MaritalStatus> getInactive() {
        return maritalStatusRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<MaritalStatus> getByAdmin(Long adminId) {
        return maritalStatusRepository.findByAdminId(adminId);
    }

    @Override
    public List<MaritalStatus> getActiveByAdmin(Long adminId) {
        return maritalStatusRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<MaritalStatus> search(String keyword) {
        return maritalStatusRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<MaritalStatus> searchByAdmin(Long adminId, String keyword) {
        return maritalStatusRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}