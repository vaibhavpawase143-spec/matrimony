package com.example.serviceimpl;

import com.example.model.SubCaste;
import com.example.repository.SubCasteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCasteServiceImpl {

    private final SubCasteRepository repo;

    public SubCasteServiceImpl(SubCasteRepository repo) {
        this.repo = repo;
    }

    // ✅ Get all active
    public List<SubCaste> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ❌ Get all inactive
    public List<SubCaste> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // 🔍 Find by name
    public Optional<SubCaste> getByName(String name) {
        return repo.findByNameIgnoreCase(name);
    }

    // 🔍 Check duplicate
    public boolean existsByName(String name) {
        return repo.existsByNameIgnoreCase(name);
    }

    // 🔍 Get by admin
    public List<SubCaste> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Active by admin
    public List<SubCaste> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Get by caste
    public List<SubCaste> getByCaste(Long casteId) {
        return repo.findByCaste_Id(casteId);
    }

    // ✅ Active by caste
    public List<SubCaste> getActiveByCaste(Long casteId) {
        return repo.findByCaste_IdAndIsActiveTrue(casteId);
    }

    // 🔍 Search
    public List<SubCaste> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // 🔍 Optional direct field
    public List<SubCaste> getByCasteId(Long casteId) {
        return repo.findByCasteId(casteId);
    }
}