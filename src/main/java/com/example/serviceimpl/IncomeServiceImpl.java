package com.example.serviceimpl;

import com.example.dto.request.IncomeRequestDTO;
import com.example.dto.response.IncomeResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Income;
import com.example.repository.AdminRepository;
import com.example.repository.IncomeRepository;
import com.example.service.IncomeService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "INCOME";

    private final IncomeRepository incomeRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public IncomeResponseDTO create(IncomeRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (incomeRepository.existsByRangeIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getRange(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Income already exists.");
        }

        Income entity = Income.builder()
                .admin(admin)
                .range(requestDto.getRange().trim())
                .isActive(requestDto.getIsActive())
                .build();

        Income saved = incomeRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                saved.getId(),
                saved.getRange(),
                saved.getRange()
        );

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public IncomeResponseDTO update(Long id,
                                    IncomeRequestDTO requestDto) {

        Income entity = incomeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found."));

        boolean oldStatus = entity.getIsActive();

        if (incomeRepository
                .findByRangeIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getRange(),
                        requestDto.getAdminId())
                .filter(i -> !i.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Income already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = entity.getRange();

        entity.setAdmin(admin);
        entity.setRange(requestDto.getRange().trim());
        entity.setIsActive(requestDto.getIsActive());

        Income updated = incomeRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                updated.getId(),
                updated.getRange(),
                oldValue,
                updated.getRange(),
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

        Income entity = incomeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found."));

        entity.setDeletedAt(LocalDateTime.now());

        incomeRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getRange(),
                entity.getRange()
        );
    }
    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        Income entity = incomeRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Income not found."));

        if (incomeRepository.existsByRangeIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                entity.getRange(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Income already exists.");
        }

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        incomeRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getRange(),
                entity.getRange()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        Income entity = incomeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getRange(),
                entity.getRange()
        );

        incomeRepository.delete(entity);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public IncomeResponseDTO getById(Long id) {

        return mapToResponse(
                incomeRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Income not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<IncomeResponseDTO> getAll() {

        return incomeRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<IncomeResponseDTO> getDeleted() {

        return incomeRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<IncomeResponseDTO> getActive() {

        return incomeRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<IncomeResponseDTO> getInactive() {

        return incomeRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<IncomeResponseDTO> getByAdmin(Long adminId) {

        return incomeRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<IncomeResponseDTO> getActiveByAdmin(Long adminId) {

        return incomeRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<IncomeResponseDTO> getInactiveByAdmin(Long adminId) {

        return incomeRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<IncomeResponseDTO> search(String keyword) {

        return incomeRepository
                .findByRangeContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<IncomeResponseDTO> searchByAdmin(Long adminId,
                                                 String keyword) {

        return incomeRepository
                .findByAdmin_IdAndRangeContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // DTO MAPPING
    // =========================

    private IncomeResponseDTO mapToResponse(Income entity) {

        return IncomeResponseDTO.builder()
                .id(entity.getId())
                .range(entity.getRange())
                .adminId(entity.getAdmin().getId())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}

