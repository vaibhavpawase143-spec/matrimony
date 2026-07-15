package com.example.serviceimpl;

import com.example.dto.request.FamilyRequestDto;
import com.example.dto.response.FamilyResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Family;
import com.example.repository.AdminRepository;
import com.example.repository.FamilyRepository;
import com.example.service.FamilyService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "FAMILY";

    private final FamilyRepository familyRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FamilyResponseDto create(FamilyRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (familyRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Family already exists.");
        }

        Family family = Family.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        Family saved = familyRepository.save(family);

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
    public FamilyResponseDto update(Long id,
                                    FamilyRequestDto requestDto) {

        Family family = familyRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family not found."));

        boolean oldStatus = family.getIsActive();

        if (familyRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(f -> !f.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Family already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = family.getName();

        family.setAdmin(admin);
        family.setName(requestDto.getName().trim());
        family.setIsActive(requestDto.getIsActive());

        Family updated = familyRepository.save(family);

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

        Family family = familyRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family not found."));

        family.setDeletedAt(LocalDateTime.now());

        familyRepository.save(family);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                family.getId(),
                family.getName(),
                family.getName()
        );
    }
    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        Family family = familyRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Family not found."));

        if (familyRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                family.getName(),
                family.getAdmin().getId())) {

            throw new BadRequestException("Family already exists.");
        }

        family.setDeletedAt(null);
        family.setDeletedBy(null);

        familyRepository.save(family);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                family.getId(),
                family.getName(),
                family.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        Family family = familyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                family.getId(),
                family.getName(),
                family.getName()
        );

        familyRepository.delete(family);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public FamilyResponseDto getById(Long id) {

        return mapToResponse(
                familyRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<FamilyResponseDto> getAll() {

        return familyRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyResponseDto> getDeleted() {

        return familyRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<FamilyResponseDto> getActive() {

        return familyRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyResponseDto> getInactive() {

        return familyRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<FamilyResponseDto> getByAdmin(Long adminId) {

        return familyRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyResponseDto> getActiveByAdmin(Long adminId) {

        return familyRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyResponseDto> getInactiveByAdmin(Long adminId) {

        return familyRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<FamilyResponseDto> search(String keyword) {

        return familyRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyResponseDto> searchByAdmin(Long adminId,
                                                 String keyword) {

        return familyRepository
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

    private FamilyResponseDto mapToResponse(Family entity) {

        return FamilyResponseDto.builder()
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