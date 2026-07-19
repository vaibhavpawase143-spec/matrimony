package com.example.serviceimpl;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.request.SubscriptionPlanRequestDTO;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.SubscriptionPlan;
import com.example.repository.AdminRepository;
import com.example.repository.SubscriptionPlanRepository;
import com.example.service.CurrentAdminService;
import com.example.service.SubscriptionPlanService;
import com.example.specification.SubscriptionPlanSpecification;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Subscription Plan";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public SubscriptionPlanResponseDTO create(
            SubscriptionPlanRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (subscriptionPlanRepository
                .existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        admin.getId())) {

            throw new BadRequestException(
                    "Subscription Plan already exists.");
        }

        SubscriptionPlan entity = SubscriptionPlan.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .price(requestDto.getPrice())
                .duration(requestDto.getDuration())
                .description(requestDto.getDescription())
                .isActive(
                        requestDto.getIsActive() != null
                                ? requestDto.getIsActive()
                                : true
                )
                .build();

        entity = subscriptionPlanRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        return mapToResponse(entity);
    }
    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public SubscriptionPlanResponseDTO update(
            Long id,
            SubscriptionPlanRequestDTO requestDto) {

        SubscriptionPlan entity = subscriptionPlanRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found."));

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && subscriptionPlanRepository
                .existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        admin.getId())) {

            throw new BadRequestException(
                    "Subscription Plan already exists.");
        }

        String oldValue =
                "Name=" + entity.getName()
                        + ", Price=" + entity.getPrice()
                        + ", Duration=" + entity.getDuration()
                        + ", Description=" + entity.getDescription();

        boolean wasActive = Boolean.TRUE.equals(entity.getIsActive());

        entity.setAdmin(admin);
        entity.setName(requestDto.getName().trim());
        entity.setPrice(requestDto.getPrice());
        entity.setDuration(requestDto.getDuration());
        entity.setDescription(requestDto.getDescription());
        entity.setIsActive(
                requestDto.getIsActive() != null
                        ? requestDto.getIsActive()
                        : entity.getIsActive()
        );

        entity = subscriptionPlanRepository.save(entity);

        String newValue =
                "Name=" + entity.getName()
                        + ", Price=" + entity.getPrice()
                        + ", Duration=" + entity.getDuration()
                        + ", Description=" + entity.getDescription();

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                oldValue,
                newValue,
                wasActive,
                entity.getIsActive()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void softDelete(Long id) {

        SubscriptionPlan entity = subscriptionPlanRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(
                currentAdminService.getCurrentAdmin().getId());

        subscriptionPlanRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @Override
    public void restore(Long id) {

        SubscriptionPlan entity = subscriptionPlanRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Deleted Subscription Plan not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        subscriptionPlanRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @Override
    public void hardDelete(Long id) {

        SubscriptionPlan entity = subscriptionPlanRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        subscriptionPlanRepository.delete(entity);
    }
    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public SubscriptionPlanResponseDTO getById(Long id) {

        SubscriptionPlan entity = subscriptionPlanRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<SubscriptionPlanResponseDTO> getAll() {

        return subscriptionPlanRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getDeleted() {

        return subscriptionPlanRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<SubscriptionPlanResponseDTO> getActive() {

        return subscriptionPlanRepository
                .findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getInactive() {

        return subscriptionPlanRepository
                .findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<SubscriptionPlanResponseDTO> getByAdmin(Long adminId) {

        return subscriptionPlanRepository
                .findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getActiveByAdmin(Long adminId) {

        return subscriptionPlanRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getInactiveByAdmin(Long adminId) {

        return subscriptionPlanRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<SubscriptionPlanResponseDTO> search(String keyword) {

        return subscriptionPlanRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubscriptionPlanResponseDTO> searchByAdmin(
            Long adminId,
            String keyword) {

        return subscriptionPlanRepository
                .findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // STATISTICS
    // =====================================================

    @Override
    public SubscriptionPlanStatsDTO getStatistics() {

        return SubscriptionPlanStatsDTO.builder()
                .totalPlans(
                        subscriptionPlanRepository.countByDeletedAtIsNull()
                )
                .activePlans(
                        subscriptionPlanRepository
                                .countByIsActiveTrueAndDeletedAtIsNull()
                )
                .inactivePlans(
                        subscriptionPlanRepository
                                .countByIsActiveFalseAndDeletedAtIsNull()
                )
                .build();
    }

    // =====================================================
    // PAGINATION
    // =====================================================

    @Override
    public Page<SubscriptionPlanResponseDTO> getAllPlans(
            SubscriptionPlanFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return subscriptionPlanRepository.findAll(
                        SubscriptionPlanSpecification.getPlans(filter),
                        pageable
                )
                .map(this::mapToResponse);
    }

    // =====================================================
    // DTO MAPPING
    // =====================================================

    private SubscriptionPlanResponseDTO mapToResponse(
            SubscriptionPlan entity) {

        return SubscriptionPlanResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null
                        ? entity.getAdmin().getId()
                        : null)
                .adminName(entity.getAdmin() != null
                        ? entity.getAdmin().getName()
                        : null)
                .name(entity.getName())
                .price(entity.getPrice())
                .duration(entity.getDuration())
                .description(entity.getDescription())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}