package com.example.serviceimpl;

import com.example.dto.request.FamilyTypeRequestDto;
import com.example.dto.response.FamilyTypeResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.FamilyType;
import com.example.repository.AdminRepository;
import com.example.repository.FamilyTypeRepository;
import com.example.service.FamilyTypeService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyTypeServiceImpl implements FamilyTypeService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "FAMILY_TYPE";

    private final FamilyTypeRepository familyTypeRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FamilyTypeResponseDto create(FamilyTypeRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (familyTypeRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Family Type already exists.");
        }

        FamilyType familyType = FamilyType.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        FamilyType saved = familyTypeRepository.save(familyType);

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
    public FamilyTypeResponseDto update(Long id,
                                        FamilyTypeRequestDto requestDto) {

        FamilyType familyType = familyTypeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Type not found."));

        boolean oldStatus = familyType.getIsActive();

        if (familyTypeRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(f -> !f.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Family Type already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = familyType.getName();

        familyType.setAdmin(admin);
        familyType.setName(requestDto.getName().trim());
        familyType.setIsActive(requestDto.getIsActive());

        FamilyType updated = familyTypeRepository.save(familyType);

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

        FamilyType familyType = familyTypeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Type not found."));

        familyType.setDeletedAt(LocalDateTime.now());

        familyTypeRepository.save(familyType);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                familyType.getId(),
                familyType.getName(),
                familyType.getName()
        );
    }

    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        FamilyType familyType = familyTypeRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Family Type not found."));

        if (familyTypeRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                familyType.getName(),
                familyType.getAdmin().getId())) {

            throw new BadRequestException("Family Type already exists.");
        }

        familyType.setDeletedAt(null);
        familyType.setDeletedBy(null);

        familyTypeRepository.save(familyType);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                familyType.getId(),
                familyType.getName(),
                familyType.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        FamilyType familyType = familyTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Type not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                familyType.getId(),
                familyType.getName(),
                familyType.getName()
        );

        familyTypeRepository.delete(familyType);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public FamilyTypeResponseDto getById(Long id) {

        return mapToResponse(
                familyTypeRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family Type not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<FamilyTypeResponseDto> getAll() {

        return familyTypeRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyTypeResponseDto> getDeleted() {

        return familyTypeRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<FamilyTypeResponseDto> getActive() {

        return familyTypeRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyTypeResponseDto> getInactive() {

        return familyTypeRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<FamilyTypeResponseDto> getByAdmin(Long adminId) {

        return familyTypeRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyTypeResponseDto> getActiveByAdmin(Long adminId) {

        return familyTypeRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyTypeResponseDto> getInactiveByAdmin(Long adminId) {

        return familyTypeRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<FamilyTypeResponseDto> search(String keyword) {

        return familyTypeRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyTypeResponseDto> searchByAdmin(Long adminId,
                                                     String keyword) {

        return familyTypeRepository
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

    private FamilyTypeResponseDto mapToResponse(FamilyType entity) {

        return FamilyTypeResponseDto.builder()
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