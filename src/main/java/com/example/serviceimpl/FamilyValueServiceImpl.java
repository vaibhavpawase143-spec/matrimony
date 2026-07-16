package com.example.serviceimpl;

import com.example.dto.request.FamilyValueRequestDto;
import com.example.dto.response.FamilyValueResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.FamilyValue;
import com.example.repository.AdminRepository;
import com.example.repository.FamilyValueRepository;
import com.example.service.FamilyValueService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyValueServiceImpl implements FamilyValueService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "FAMILY_VALUE";

    private final FamilyValueRepository familyValueRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FamilyValueResponseDto create(FamilyValueRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (familyValueRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Family Value already exists.");
        }

        FamilyValue familyValue = FamilyValue.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        FamilyValue saved = familyValueRepository.save(familyValue);

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
    public FamilyValueResponseDto update(Long id,
                                         FamilyValueRequestDto requestDto) {

        FamilyValue familyValue = familyValueRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Value not found."));

        boolean oldStatus = familyValue.getIsActive();

        if (familyValueRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(f -> !f.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Family Value already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = familyValue.getName();

        familyValue.setAdmin(admin);
        familyValue.setName(requestDto.getName().trim());
        familyValue.setIsActive(requestDto.getIsActive());

        FamilyValue updated = familyValueRepository.save(familyValue);

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

        FamilyValue familyValue = familyValueRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Value not found."));

        familyValue.setDeletedAt(LocalDateTime.now());

        familyValueRepository.save(familyValue);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                familyValue.getId(),
                familyValue.getName(),
                familyValue.getName()
        );
    }

    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        FamilyValue familyValue = familyValueRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Family Value not found."));

        if (familyValueRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                familyValue.getName(),
                familyValue.getAdmin().getId())) {

            throw new BadRequestException("Family Value already exists.");
        }

        familyValue.setDeletedAt(null);
        familyValue.setDeletedBy(null);

        familyValueRepository.save(familyValue);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                familyValue.getId(),
                familyValue.getName(),
                familyValue.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        FamilyValue familyValue = familyValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Value not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                familyValue.getId(),
                familyValue.getName(),
                familyValue.getName()
        );

        familyValueRepository.delete(familyValue);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public FamilyValueResponseDto getById(Long id) {

        return mapToResponse(
                familyValueRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family Value not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<FamilyValueResponseDto> getAll() {

        return familyValueRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyValueResponseDto> getDeleted() {

        return familyValueRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<FamilyValueResponseDto> getActive() {

        return familyValueRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyValueResponseDto> getInactive() {

        return familyValueRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<FamilyValueResponseDto> getByAdmin(Long adminId) {

        return familyValueRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyValueResponseDto> getActiveByAdmin(Long adminId) {

        return familyValueRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyValueResponseDto> getInactiveByAdmin(Long adminId) {

        return familyValueRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<FamilyValueResponseDto> search(String keyword) {

        return familyValueRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyValueResponseDto> searchByAdmin(Long adminId,
                                                      String keyword) {

        return familyValueRepository
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

    private FamilyValueResponseDto mapToResponse(FamilyValue entity) {

        return FamilyValueResponseDto.builder()
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