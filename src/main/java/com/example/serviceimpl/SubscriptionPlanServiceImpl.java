package com.example.serviceimpl;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import com.example.model.Admin;
import com.example.model.SubscriptionPlan;
import com.example.repository.SubscriptionPlanRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.CurrentAdminService;
import com.example.service.SubscriptionPlanService;
import com.example.specification.SubscriptionPlanSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository repository;
    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService auditLogService;

    public SubscriptionPlanServiceImpl(
            SubscriptionPlanRepository repository,
            CurrentAdminService currentAdminService,
            AdminAuditLogService auditLogService) {
        this.repository = repository;
        this.currentAdminService = currentAdminService;
        this.auditLogService = auditLogService;
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
        SubscriptionPlan saved = repository.save(plan);

        Admin currentAdmin = currentAdminService.getCurrentAdmin();
        auditLogService.log(
                currentAdmin.getId(),
                "SUBSCRIPTION_MANAGEMENT",
                "PLAN_CREATED",
                "SUBSCRIPTION_PLAN",
                saved.getId(),
                "Created subscription plan: " + saved.getName(),
                null,
                "Name=" + saved.getName() + ", Price=" + saved.getPrice() + ", Duration=" + saved.getDuration() + " days, Active=" + saved.getIsActive()
        );

        return saved;
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

        // Capture transitions for active status
        String action = "PLAN_UPDATED";
        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());
        boolean nowActive = plan.getIsActive() != null ? plan.getIsActive() : true;

        if (!wasActive && nowActive) {
            action = "PLAN_ACTIVATED";
        } else if (wasActive && !nowActive) {
            action = "PLAN_DEACTIVATED";
        }

        String oldValueDescription = "Name=" + existing.getName() + ", Price=" + existing.getPrice() + ", Duration=" + existing.getDuration() + " days, Active=" + existing.getIsActive();

        existing.setName(plan.getName());
        existing.setPrice(plan.getPrice());
        existing.setDuration(plan.getDuration());
        existing.setDescription(plan.getDescription());
        existing.setIsActive(plan.getIsActive());

        SubscriptionPlan saved = repository.save(existing);

        String newValueDescription = "Name=" + saved.getName() + ", Price=" + saved.getPrice() + ", Duration=" + saved.getDuration() + " days, Active=" + saved.getIsActive();

        Admin currentAdmin = currentAdminService.getCurrentAdmin();
        auditLogService.log(
                currentAdmin.getId(),
                "SUBSCRIPTION_MANAGEMENT",
                action,
                "SUBSCRIPTION_PLAN",
                saved.getId(),
                "Updated subscription plan: " + saved.getName(),
                oldValueDescription,
                newValueDescription
        );

        return saved;
    }

    // ❌ Soft delete
    @Override
    public void delete(Long id) {

        SubscriptionPlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found!"));

        boolean wasActive = Boolean.TRUE.equals(plan.getIsActive());
        plan.setIsActive(false);
        SubscriptionPlan saved = repository.save(plan);

        if (wasActive) {
            Admin currentAdmin = currentAdminService.getCurrentAdmin();
            auditLogService.log(
                    currentAdmin.getId(),
                    "SUBSCRIPTION_MANAGEMENT",
                    "PLAN_DEACTIVATED",
                    "SUBSCRIPTION_PLAN",
                    saved.getId(),
                    "Soft deleted (deactivated) subscription plan: " + saved.getName(),
                    "Active=true",
                    "Active=false"
            );
        }
    }
    // 📊 Statistics
    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanStatsDTO getStatistics() {

        return SubscriptionPlanStatsDTO.builder()
                .totalPlans(repository.count())
                .activePlans(repository.countByIsActiveTrue())
                .inactivePlans(repository.countByIsActiveFalse())
                .build();
    }
    @Override
    public Page<SubscriptionPlanResponseDTO> getAllPlans(
            SubscriptionPlanFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(
                SubscriptionPlanSpecification.getPlans(filter),
                pageable
        ).map(plan -> SubscriptionPlanResponseDTO.builder()
                .id(plan.getId())
                .adminId(plan.getAdmin() != null ? plan.getAdmin().getId() : null)
                .adminName(plan.getAdmin() != null ? plan.getAdmin().getName() : null)
                .name(plan.getName())
                .price(plan.getPrice())
                .duration(plan.getDuration())
                .description(plan.getDescription())
                .isActive(plan.getIsActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build());
    }
}