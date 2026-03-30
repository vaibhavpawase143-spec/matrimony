package com.example.serviceimpl;

import com.example.model.Weight;
import com.example.repository.WeightRepository;
import com.example.service.WeightService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeightServiceImpl implements WeightService {

    private final WeightRepository repository;

    public WeightServiceImpl(WeightRepository repository) {
        this.repository = repository;
    }

    // =========================
    // ✅ SAVE (WITH DUPLICATE CHECK)
    // =========================
    @Override
    public Weight save(Weight weight) {

        if (weight.getAdmin() == null || weight.getAdmin().getId() == null) {
            throw new RuntimeException("Admin is required");
        }

        String value = weight.getValue();
        Long adminId = weight.getAdmin().getId();

        Optional<Weight> existing =
                repository.findByValueIgnoreCaseAndAdminId(value, adminId);

        if (existing.isPresent() &&
                (weight.getId() == null || !existing.get().getId().equals(weight.getId()))) {
            throw new RuntimeException("Weight already exists for this admin!");
        }

        return repository.save(weight);
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @Override
    public Optional<Weight> getById(Long id) {
        return repository.findById(id);
    }

    // =========================
    // 🔍 GET ALL
    // =========================
    @Override
    public List<Weight> getAll() {
        return repository.findAll();
    }

    // =========================
    // 🔍 GET BY ADMIN
    // =========================
    @Override
    public List<Weight> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // =========================
    // 🔍 ACTIVE BY ADMIN
    // =========================
    @Override
    public List<Weight> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // =========================
    // 🔍 INACTIVE BY ADMIN
    // =========================
    @Override
    public List<Weight> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // =========================
    // 🔍 SEARCH BY ADMIN
    // =========================
    @Override
    public List<Weight> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }

    // =========================
    // 🔍 FIND BY VALUE + ADMIN
    // =========================
    @Override
    public Optional<Weight> getByValueAndAdmin(String value, Long adminId) {
        return repository.findByValueIgnoreCaseAndAdminId(value, adminId);
    }

    // =========================
    // ❌ DELETE (SOFT DELETE + BASIC OWNERSHIP)
    // =========================
    @Override
    public void delete(Long id) {

        Weight weight = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weight not found!"));

        // 🔥 GET LOGGED-IN USER EMAIL (for future enhancement)
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // ⚠️ BASIC SAFETY CHECK
        if (weight.getAdmin() == null || weight.getAdmin().getId() == null) {
            throw new RuntimeException("Invalid data: admin not found");
        }

        // 👉 Since no user mapping, we allow operation but keep structure ready
        weight.setIsActive(false);

        repository.save(weight);
    }
}