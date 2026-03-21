package com.example.serviceimpl;

import com.example.model.FamilyStatus;
import com.example.repository.FamilyStatusRepository;
import com.example.service.FamilyStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyStatusServiceImpl implements FamilyStatusService {

    @Autowired
    private FamilyStatusRepository repository;

    @Override
    public FamilyStatus save(FamilyStatus familyStatus) {
        return repository.save(familyStatus);
    }

    @Override
    public Optional<FamilyStatus> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<FamilyStatus> getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<FamilyStatus> getAll() {
        return repository.findAll();
    }

    @Override
    public List<FamilyStatus> getAllActive() {
        return repository.findByActiveTrue();
    }

    @Override
    public List<FamilyStatus> getAllInactive() {
        return repository.findByActiveFalse();
    }

    @Override
    public List<FamilyStatus> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    @Override
    public List<FamilyStatus> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndActiveTrue(adminId);
    }

    @Override
    public List<FamilyStatus> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public FamilyStatus update(Long id, FamilyStatus updated) {
        FamilyStatus existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found"));

        existing.setName(updated.getName());
        existing.setActive(updated.getActive());
        existing.setAdminId(updated.getAdminId());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        FamilyStatus existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found"));

        existing.setActive(false); // soft delete
        repository.save(existing);
    }
}