package com.example.serviceimpl;

import com.example.dto.request.EmployedRequestDto;
import com.example.dto.response.EmployedResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Employed;
import com.example.repository.AdminRepository;
import com.example.repository.EmployedRepository;
import com.example.service.EmployedService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployedServiceImpl implements EmployedService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "EMPLOYED";

    private final EmployedRepository employedRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public EmployedResponseDto create(EmployedRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (employedRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Employed already exists.");
        }

        Employed employed = Employed.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        Employed saved = employedRepository.save(employed);

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
    public EmployedResponseDto update(Long id,
                                      EmployedRequestDto requestDto) {

        Employed employed = employedRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employed not found."));

        boolean oldStatus = employed.getIsActive();

        if (employedRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(e -> !e.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Employed already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = employed.getName();

        employed.setAdmin(admin);
        employed.setName(requestDto.getName().trim());
        employed.setIsActive(requestDto.getIsActive());

        Employed updated = employedRepository.save(employed);

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

        Employed employed = employedRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employed not found."));

        employed.setDeletedAt(LocalDateTime.now());

        employedRepository.save(employed);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                employed.getId(),
                employed.getName(),
                employed.getName()
        );
    }
    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        Employed employed = employedRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Employed not found."));

        if (employedRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                employed.getName(),
                employed.getAdmin().getId())) {

            throw new BadRequestException("Employed already exists.");
        }

        employed.setDeletedAt(null);
        employed.setDeletedBy(null);

        employedRepository.save(employed);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                employed.getId(),
                employed.getName(),
                employed.getName()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        Employed employed = employedRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employed not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                employed.getId(),
                employed.getName(),
                employed.getName()
        );

        employedRepository.delete(employed);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public EmployedResponseDto getById(Long id) {

        return mapToResponse(
                employedRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Employed not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<EmployedResponseDto> getAll() {

        return employedRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployedResponseDto> getDeleted() {

        return employedRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<EmployedResponseDto> getActive() {

        return employedRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployedResponseDto> getInactive() {

        return employedRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<EmployedResponseDto> getByAdmin(Long adminId) {

        return employedRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployedResponseDto> getActiveByAdmin(Long adminId) {

        return employedRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployedResponseDto> getInactiveByAdmin(Long adminId) {

        return employedRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<EmployedResponseDto> search(String keyword) {

        return employedRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployedResponseDto> searchByAdmin(Long adminId,
                                                   String keyword) {

        return employedRepository
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

    private EmployedResponseDto mapToResponse(Employed entity) {

        return EmployedResponseDto.builder()
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