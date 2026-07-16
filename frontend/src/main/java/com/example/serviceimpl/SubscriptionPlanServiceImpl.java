package com.example.serviceimpl;

import com.example.model.SubscriptionPlan;
import com.example.repository.SubscriptionPlanRepository;
import com.example.service.SubscriptionPlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository repository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository repository) {
        this.repository = repository;
    }

    // ✅ Create
    @Override
    public SubscriptionPlan create(SubscriptionPlan plan) {

        String name = plan.getName();
        Long adminId = plan.getAdmin().getId();

        if (repository.existsByNameIgnoreCaseAndAdminId(name, adminId)) {
            throw new RuntimeException("Subscription plan already exists!");
        }

        plan.setIsActive(true);
        return repository.save(plan);
    }

    // 🔍 Get by ID
    @Override
    public Optional<SubscriptionPlan> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<SubscriptionPlan> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<SubscriptionPlan> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active by admin
    @Override
    public List<SubscriptionPlan> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Inactive by admin
    @Override
    public List<SubscriptionPlan> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search (admin-based)
    @Override
    public List<SubscriptionPlan> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // ✅ Update
    @Override
    public SubscriptionPlan update(Long id, SubscriptionPlan plan) {

        SubscriptionPlan existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found!"));

        String name = plan.getName();
        Long adminId = plan.getAdmin().getId();

        Optional<SubscriptionPlan> duplicate =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (duplicate.isPresent() &&
                !duplicate.get().getId().equals(id)) {
            throw new RuntimeException("Subscription plan already exists!");
        }

        existing.setName(plan.getName());
        existing.setPrice(plan.getPrice());
        existing.setDuration(plan.getDuration());
        existing.setDescription(plan.getDescription());
        existing.setIsActive(plan.getIsActive());

        return repository.save(existing);
    }

    // ❌ Soft delete
    @Override
    public void delete(Long id) {

        SubscriptionPlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found!"));

        plan.setIsActive(false);
        repository.save(plan);
    }
}