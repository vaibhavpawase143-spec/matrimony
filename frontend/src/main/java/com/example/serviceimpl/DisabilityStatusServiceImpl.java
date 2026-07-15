package com.example.serviceimpl;

import com.example.model.DisabilityStatus;
import com.example.repository.DisabilityStatusRepository;
import com.example.service.DisabilityStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisabilityStatusServiceImpl implements DisabilityStatusService {

    private final DisabilityStatusRepository disabilityStatusRepository;

    public DisabilityStatusServiceImpl(DisabilityStatusRepository disabilityStatusRepository) {
        this.disabilityStatusRepository = disabilityStatusRepository;
    }

    // ✅ Create
    @Override
    public DisabilityStatus create(DisabilityStatus disabilityStatus) {

        if (disabilityStatusRepository.existsByValueIgnoreCase(disabilityStatus.getValue())) {
            throw new RuntimeException("Disability status already exists: " + disabilityStatus.getValue());
        }

        return disabilityStatusRepository.save(disabilityStatus);
    }

    // 🔄 Update
    @Override
    public DisabilityStatus update(Long id, DisabilityStatus disabilityStatus) {

        DisabilityStatus existing = disabilityStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disability status not found with id: " + id));

        disabilityStatusRepository.findByValueIgnoreCase(disabilityStatus.getValue())
                .ifPresent(d -> {
                    if (!d.getId().equals(id)) {
                        throw new RuntimeException("Disability status already exists: " + disabilityStatus.getValue());
                    }
                });

        // ✏️ Update fields
        existing.setValue(disabilityStatus.getValue());
        existing.setIsActive(disabilityStatus.getIsActive());

        return disabilityStatusRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        DisabilityStatus existing = disabilityStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disability status not found with id: " + id));

        disabilityStatusRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<DisabilityStatus> getById(Long id) {
        return disabilityStatusRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<DisabilityStatus> getAll() {
        return disabilityStatusRepository.findAll();
    }

    // 🔍 Find by value
    @Override
    public Optional<DisabilityStatus> getByValue(String value) {
        return disabilityStatusRepository.findByValue(value);
    }

    @Override
    public Optional<DisabilityStatus> getByValueIgnoreCase(String value) {
        return disabilityStatusRepository.findByValueIgnoreCase(value);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByValue(String value) {
        return disabilityStatusRepository.existsByValue(value);
    }

    @Override
    public boolean existsByValueIgnoreCase(String value) {
        return disabilityStatusRepository.existsByValueIgnoreCase(value);
    }

    // 🔍 Active / Inactive
    @Override
    public List<DisabilityStatus> getActive() {
        return disabilityStatusRepository.findByIsActiveTrue();
    }

    @Override
    public List<DisabilityStatus> getInactive() {
        return disabilityStatusRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<DisabilityStatus> getByAdmin(Long adminId) {
        return disabilityStatusRepository.findByAdminId(adminId);
    }

    @Override
    public List<DisabilityStatus> getActiveByAdmin(Long adminId) {
        return disabilityStatusRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<DisabilityStatus> search(String keyword) {
        return disabilityStatusRepository.findByValueContainingIgnoreCase(keyword);
    }

    @Override
    public List<DisabilityStatus> searchByAdmin(Long adminId, String keyword) {
        return disabilityStatusRepository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }
}