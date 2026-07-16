package com.example.serviceimpl;

import com.example.dto.request.WeightRequestDTO;
import com.example.dto.response.WeightResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Weight;
import com.example.repository.AdminRepository;
import com.example.repository.WeightRepository;
import com.example.service.CurrentAdminService;
import com.example.service.WeightService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightServiceImpl implements WeightService {

    private final WeightRepository weightRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Weight";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public WeightResponseDTO create(WeightRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (weightRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getValue(),
                admin.getId())) {

            throw new BadRequestException("Weight already exists.");
        }

        Weight entity = Weight.builder()
                .admin(admin)
                .value(requestDto.getValue().trim())
                .isActive(
                        requestDto.getIsActive() != null
                                ? requestDto.getIsActive()
                                : true
                )
                .build();

        entity = weightRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public WeightResponseDTO update(Long id,
                                    WeightRequestDTO requestDto) {

        Weight entity = weightRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Weight not found."));

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (!entity.getValue().equalsIgnoreCase(requestDto.getValue())
                &&
                weightRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getValue(),
                        admin.getId())) {

            throw new BadRequestException("Weight already exists.");
        }

        String oldValue = entity.getValue();
        Boolean oldStatus = entity.getIsActive();

        entity.setAdmin(admin);
        entity.setValue(requestDto.getValue().trim());
        entity.setIsActive(requestDto.getIsActive());

        entity = weightRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                oldValue,
                entity.getValue(),
                oldStatus,
                entity.getIsActive()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void softDelete(Long id) {

        Weight entity = weightRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Weight not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        weightRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @Override
    public void restore(Long id) {

        Weight entity = weightRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Weight not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        weightRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @Override
    public void hardDelete(Long id) {

        Weight entity = weightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Weight not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );

        weightRepository.delete(entity);
    }
    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public WeightResponseDTO getById(Long id) {

        Weight entity = weightRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Weight not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<WeightResponseDTO> getAll() {

        return weightRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeightResponseDTO> getDeleted() {

        return weightRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<WeightResponseDTO> getActive() {

        return weightRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeightResponseDTO> getInactive() {

        return weightRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<WeightResponseDTO> getByAdmin(Long adminId) {

        return weightRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeightResponseDTO> getActiveByAdmin(Long adminId) {

        return weightRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeightResponseDTO> getInactiveByAdmin(Long adminId) {

        return weightRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<WeightResponseDTO> search(String keyword) {

        return weightRepository
                .findByValueContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeightResponseDTO> searchByAdmin(Long adminId,
                                                 String keyword) {

        return weightRepository
                .findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // DTO MAPPING
    // =====================================================

    private WeightResponseDTO mapToResponse(Weight entity) {

        return WeightResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null
                        ? entity.getAdmin().getId()
                        : null)
                .adminName(entity.getAdmin() != null
                        ? entity.getAdmin().getName()
                        : null)
                .name(entity.getValue())
                .value(entity.getValue())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}