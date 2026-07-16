package com.example.serviceimpl;

import com.example.dto.request.SisterTypeRequestDTO;
import com.example.dto.response.SisterTypeResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.SisterType;
import com.example.repository.AdminRepository;
import com.example.repository.SisterTypeRepository;
import com.example.service.CurrentAdminService;
import com.example.service.SisterTypeService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SisterTypeServiceImpl implements SisterTypeService {

    private final SisterTypeRepository sisterTypeRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Sister Type";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public SisterTypeResponseDTO create(SisterTypeRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (sisterTypeRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getValue(),
                admin.getId())) {

            throw new BadRequestException("Sister Type already exists.");
        }

        SisterType entity = SisterType.builder()
                .admin(admin)
                .value(requestDto.getValue())
                .isActive(requestDto.getIsActive())
                .build();

        entity = sisterTypeRepository.save(entity);

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
    public SisterTypeResponseDTO update(Long id,
                                        SisterTypeRequestDTO requestDto) {

        SisterType entity = sisterTypeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sister Type not found."));

        if (!entity.getValue().equalsIgnoreCase(requestDto.getValue())
                && sisterTypeRepository.existsByValueIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getValue(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Sister Type already exists.");
        }

        String oldValue = entity.getValue();
        Boolean oldActive = entity.getIsActive();

        entity.setValue(requestDto.getValue());
        entity.setIsActive(requestDto.getIsActive());

        entity = sisterTypeRepository.save(entity);

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

        SisterType entity = sisterTypeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sister Type not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        sisterTypeRepository.save(entity);

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

        SisterType entity = sisterTypeRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Sister Type not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        sisterTypeRepository.save(entity);

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

        SisterType entity = sisterTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sister Type not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getValue(),
                entity.getValue()
        );

        sisterTypeRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public SisterTypeResponseDTO getById(Long id) {

        SisterType entity = sisterTypeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sister Type not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<SisterTypeResponseDTO> getAll() {

        return sisterTypeRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SisterTypeResponseDTO> getDeleted() {

        return sisterTypeRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<SisterTypeResponseDTO> getActive() {

        return sisterTypeRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SisterTypeResponseDTO> getInactive() {

        return sisterTypeRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<SisterTypeResponseDTO> getByAdmin(Long adminId) {

        return sisterTypeRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SisterTypeResponseDTO> getActiveByAdmin(Long adminId) {

        return sisterTypeRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SisterTypeResponseDTO> getInactiveByAdmin(Long adminId) {

        return sisterTypeRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<SisterTypeResponseDTO> search(String keyword) {

        return sisterTypeRepository
                .findByValueContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SisterTypeResponseDTO> searchByAdmin(Long adminId,
                                                     String keyword) {

        return sisterTypeRepository
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

    private SisterTypeResponseDTO mapToResponse(SisterType entity) {

        return SisterTypeResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .adminName(null) // Avoid LazyInitializationException
                .value(entity.getValue())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}