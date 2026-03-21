package com.example.serviceimpl;

import com.example.model.DisabilityStatus;
import com.example.repository.DisabilityStatusRepository;
import com.example.service.DisabilityStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisabilityStatusServiceImpl implements DisabilityStatusService {

    private final DisabilityStatusRepository repo;

    // Constructor Injection
    public DisabilityStatusServiceImpl(DisabilityStatusRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<DisabilityStatus> getAll() {
        return repo.findAll();
    }

    @Override
    public List<DisabilityStatus> getActive() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public DisabilityStatus getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public DisabilityStatus create(DisabilityStatus ds) {
        return repo.save(ds);
    }

    @Override
    public DisabilityStatus update(Long id, DisabilityStatus updated) {
        DisabilityStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existing.setValue(updated.getValue());
        existing.setIsActive(updated.getIsActive());
        existing.setAdminId(updated.getAdminId());

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        DisabilityStatus existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existing.setIsActive(false);
        repo.save(existing);
    }

    @Override
    public List<DisabilityStatus> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    @Override
    public List<DisabilityStatus> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<DisabilityStatus> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}