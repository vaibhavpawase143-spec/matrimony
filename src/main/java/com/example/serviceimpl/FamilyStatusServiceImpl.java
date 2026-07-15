package com.example.serviceimpl;

import com.example.dto.request.FamilyStatusRequestDto;
import com.example.dto.response.FamilyStatusResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.FamilyStatus;
import com.example.repository.AdminRepository;
import com.example.repository.FamilyStatusRepository;
import com.example.service.FamilyStatusService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyStatusServiceImpl implements FamilyStatusService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "FAMILY_STATUS";

    private final FamilyStatusRepository familyStatusRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FamilyStatusResponseDto create(FamilyStatusRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (familyStatusRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Family Status already exists.");
        }

        FamilyStatus familyStatus = FamilyStatus.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        FamilyStatus saved = familyStatusRepository.save(familyStatus);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                saved.getId(),
                saved.getName(),
                saved.getName()
        );

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public FamilyStatusResponseDto update(Long id,
                                          FamilyStatusRequestDto requestDto) {

        FamilyStatus familyStatus = familyStatusRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Status not found."));

        boolean oldStatus = familyStatus.getIsActive();

        if (familyStatusRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(f -> !f.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Family Status already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = familyStatus.getName();

        familyStatus.setAdmin(admin);
        familyStatus.setName(requestDto.getName().trim());
        familyStatus.setIsActive(requestDto.getIsActive());

        FamilyStatus updated = familyStatusRepository.save(familyStatus);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                updated.getId(),
                updated.getName(),
                oldValue,
                updated.getName(),
                oldStatus,
                updated.getIsActive()
        );

        return mapToResponse(updated);
    }

    // =========================
    // SOFT DELETE
    // =========================

    @Override
    public void softDelete(Long id) {

        FamilyStatus familyStatus = familyStatusRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Status not found."));

        familyStatus.setDeletedAt(LocalDateTime.now());

        familyStatusRepository.save(familyStatus);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                familyStatus.getId(),
                familyStatus.getName(),
                familyStatus.getName()
        );
    }
    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        FamilyStatus familyStatus = familyStatusRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Family Status not found."));

        if (familyStatusRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                familyStatus.getName(),
                familyStatus.getAdmin().getId())) {

            throw new BadRequestException("Family Status already exists.");
        }

        familyStatus.setDeletedAt(null);
        familyStatus.setDeletedBy(null);

        familyStatusRepository.save(familyStatus);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                familyStatus.getId(),
                familyStatus.getName(),
                familyStatus.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        FamilyStatus familyStatus = familyStatusRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Status not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                familyStatus.getId(),
                familyStatus.getName(),
                familyStatus.getName()
        );

        familyStatusRepository.delete(familyStatus);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public FamilyStatusResponseDto getById(Long id) {

        return mapToResponse(
                familyStatusRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family Status not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<FamilyStatusResponseDto> getAll() {

        return familyStatusRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyStatusResponseDto> getDeleted() {

        return familyStatusRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<FamilyStatusResponseDto> getActive() {

        return familyStatusRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyStatusResponseDto> getInactive() {

        return familyStatusRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<FamilyStatusResponseDto> getByAdmin(Long adminId) {

        return familyStatusRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyStatusResponseDto> getActiveByAdmin(Long adminId) {

        return familyStatusRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyStatusResponseDto> getInactiveByAdmin(Long adminId) {

        return familyStatusRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<FamilyStatusResponseDto> search(String keyword) {

        return familyStatusRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyStatusResponseDto> searchByAdmin(Long adminId,
                                                       String keyword) {

        return familyStatusRepository
                .findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // DTO MAPPING
    // =========================

    private FamilyStatusResponseDto mapToResponse(FamilyStatus entity) {

        return FamilyStatusResponseDto.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin().getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}