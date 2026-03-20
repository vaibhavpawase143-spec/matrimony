package com.example.serviceimpl;

import com.example.model.SubscriptionPlan;
import com.example.repository.SubscriptionPlanRepository;
import com.example.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository repo;

    // ✅ Create
    @Override
    public SubscriptionPlan create(SubscriptionPlan plan) {
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        plan.setIsActive(true);
        return repo.save(plan);
    }

    // ✅ Get all
    @Override
    public List<SubscriptionPlan> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<SubscriptionPlan> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get all inactive
    @Override
    public List<SubscriptionPlan> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    // ✅ Get by ID
    @Override
    public Optional<SubscriptionPlan> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public SubscriptionPlan update(Long id, SubscriptionPlan plan) {
        SubscriptionPlan existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan not found with id: " + id));

        existing.setName(plan.getName());
        existing.setPrice(plan.getPrice());
        existing.setDuration(plan.getDuration());
        existing.setDescription(plan.getDescription());
        existing.setIsActive(plan.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        SubscriptionPlan existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubscriptionPlan not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<SubscriptionPlan> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by admin
    @Override
    public List<SubscriptionPlan> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<SubscriptionPlan> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }
}