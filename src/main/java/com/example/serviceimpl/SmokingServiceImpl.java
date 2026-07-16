package com.example.serviceimpl;

import com.example.dto.request.SmokingRequestDTO;
import com.example.dto.response.SmokingResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Smoking;
import com.example.repository.AdminRepository;
import com.example.repository.SmokingRepository;
import com.example.service.CurrentAdminService;
import com.example.service.SmokingService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmokingServiceImpl implements SmokingService {

    private final SmokingRepository smokingRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Smoking";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public SmokingResponseDTO create(SmokingRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (smokingRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getValue(),
                admin.getId())) {

            throw new BadRequestException("Smoking already exists.");
        }

        Smoking entity = Smoking.builder()
                .admin(admin)
                .value(requestDto.getValue())
                .isActive(requestDto.getIsActive())
                .build();

        entity = smokingRepository.save(entity);

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
    public SmokingResponseDTO update(Long id,
                                     SmokingRequestDTO requestDto) {

        Smoking entity = smokingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Smoking not found."));

        if (!entity.getValue().equalsIgnoreCase(requestDto.getValue())
                && smokingRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getValue(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Smoking already exists.");
        }

        String oldValue = entity.getValue();
        Boolean oldActive = entity.getIsActive();

        entity.setValue(requestDto.getValue());
        entity.setIsActive(requestDto.getIsActive());

        entity = smokingRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                oldValue,
                entity.getValue(),
                oldActive,
                entity.getIsActive()
        );

        return mapToResponse(entity);
    }
    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void softDelete(Long id) {

        Smoking entity = smokingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Smoking not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        smokingRepository.save(entity);

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

        Smoking entity = smokingRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Smoking not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        smokingRepository.save(entity);

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

        Smoking entity = smokingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Smoking not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );

        smokingRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public SmokingResponseDTO getById(Long id) {

        Smoking entity = smokingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Smoking not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<SmokingResponseDTO> getAll() {

        return smokingRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SmokingResponseDTO> getDeleted() {

        return smokingRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<SmokingResponseDTO> getActive() {

        return smokingRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SmokingResponseDTO> getInactive() {

        return smokingRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<SmokingResponseDTO> getByAdmin(Long adminId) {

        return smokingRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SmokingResponseDTO> getActiveByAdmin(Long adminId) {

        return smokingRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SmokingResponseDTO> getInactiveByAdmin(Long adminId) {

        return smokingRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<SmokingResponseDTO> search(String keyword) {

        return smokingRepository
                .findByValueContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SmokingResponseDTO> searchByAdmin(Long adminId,
                                                  String keyword) {

        return smokingRepository
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

    private SmokingResponseDTO mapToResponse(Smoking entity) {

        return SmokingResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .adminName(null)
                .value(entity.getValue())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}