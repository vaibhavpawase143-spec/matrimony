package com.example.serviceimpl;

import com.example.model.DisabilityStatus;
import com.example.repository.DisabilityStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisabilityStatusServiceImpl {

    private final DisabilityStatusRepository repo;

    // Constructor Injection
    public DisabilityStatusServiceImpl(DisabilityStatusRepository repo) {
        this.repo = repo;
    }

    // ✅ Get all active records
    public List<DisabilityStatus> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ❌ Get all inactive records
    public List<DisabilityStatus> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // 🔍 Find by value
    public Optional<DisabilityStatus> getByValue(String value) {
        return repo.findByValue(value);
    }

    // 🔍 Check duplicate
    public boolean existsByValue(String value) {
        return repo.existsByValue(value);
    }

    // 🔍 Get records by adminId
    public List<DisabilityStatus> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active records by adminId
    public List<DisabilityStatus> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search by keyword
    public List<DisabilityStatus> searchByKeyword(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}