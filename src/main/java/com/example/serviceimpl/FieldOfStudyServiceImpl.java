package com.example.serviceimpl;

import com.example.dto.request.FieldOfStudyRequestDTO;
import com.example.dto.response.FieldOfStudyResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.FieldOfStudy;
import com.example.repository.AdminRepository;
import com.example.repository.FieldOfStudyRepository;
import com.example.service.FieldOfStudyService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FieldOfStudyServiceImpl implements FieldOfStudyService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "FIELD_OF_STUDY";

    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FieldOfStudyResponseDTO create(FieldOfStudyRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (fieldOfStudyRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Field Of Study already exists.");
        }

        FieldOfStudy entity = FieldOfStudy.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        FieldOfStudy saved = fieldOfStudyRepository.save(entity);

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
    public FieldOfStudyResponseDTO update(Long id,
                                          FieldOfStudyRequestDTO requestDto) {

        FieldOfStudy entity = fieldOfStudyRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Field Of Study not found."));

        boolean oldStatus = entity.getIsActive();

        if (fieldOfStudyRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(e -> !e.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Field Of Study already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = entity.getName();

        entity.setAdmin(admin);
        entity.setName(requestDto.getName().trim());
        entity.setIsActive(requestDto.getIsActive());

        FieldOfStudy updated = fieldOfStudyRepository.save(entity);

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

        FieldOfStudy entity = fieldOfStudyRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Field Of Study not found."));

        entity.setDeletedAt(LocalDateTime.now());

        fieldOfStudyRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }

    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        FieldOfStudy entity = fieldOfStudyRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Field Of Study not found."));

        if (fieldOfStudyRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                entity.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Field Of Study already exists.");
        }

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        fieldOfStudyRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        FieldOfStudy entity = fieldOfStudyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Field Of Study not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        fieldOfStudyRepository.delete(entity);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public FieldOfStudyResponseDTO getById(Long id) {

        return mapToResponse(
                fieldOfStudyRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Field Of Study not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<FieldOfStudyResponseDTO> getAll() {

        return fieldOfStudyRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FieldOfStudyResponseDTO> getDeleted() {

        return fieldOfStudyRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<FieldOfStudyResponseDTO> getActive() {

        return fieldOfStudyRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FieldOfStudyResponseDTO> getInactive() {

        return fieldOfStudyRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<FieldOfStudyResponseDTO> getByAdmin(Long adminId) {

        return fieldOfStudyRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FieldOfStudyResponseDTO> getActiveByAdmin(Long adminId) {

        return fieldOfStudyRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FieldOfStudyResponseDTO> getInactiveByAdmin(Long adminId) {

        return fieldOfStudyRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<FieldOfStudyResponseDTO> search(String keyword) {

        return fieldOfStudyRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FieldOfStudyResponseDTO> searchByAdmin(Long adminId,
                                                       String keyword) {

        return fieldOfStudyRepository
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

    private FieldOfStudyResponseDTO mapToResponse(FieldOfStudy entity) {

        return FieldOfStudyResponseDTO.builder()
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