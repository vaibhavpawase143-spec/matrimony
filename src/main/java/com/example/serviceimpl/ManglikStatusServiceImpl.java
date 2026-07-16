package com.example.serviceimpl;

import com.example.dto.request.ManglikStatusRequestDTO;
import com.example.dto.response.ManglikStatusResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.ManglikStatus;
import com.example.repository.AdminRepository;
import com.example.repository.ManglikStatusRepository;
import com.example.service.CurrentAdminService;
import com.example.service.ManglikStatusService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManglikStatusServiceImpl implements ManglikStatusService {

    private final ManglikStatusRepository manglikStatusRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Manglik Status";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public ManglikStatusResponseDTO create(ManglikStatusRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found."));

        if (manglikStatusRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Manglik Status already exists.");
        }

        ManglikStatus entity = ManglikStatus.builder()
                .admin(admin)
                .name(requestDto.getName())
                .isActive(requestDto.getIsActive())
                .build();

        entity = manglikStatusRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public ManglikStatusResponseDTO update(Long id,
                                           ManglikStatusRequestDTO requestDto) {

        ManglikStatus entity = manglikStatusRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manglik Status not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && manglikStatusRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Manglik Status already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName());
        entity.setIsActive(requestDto.getIsActive());

        entity = manglikStatusRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                oldValue,
                entity.getName(),
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

        ManglikStatus entity = manglikStatusRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manglik Status not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        manglikStatusRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }
    // =====================================================
    // RESTORE
    // =====================================================

    @Override
    public void restore(Long id) {

        ManglikStatus entity = manglikStatusRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Manglik Status not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        manglikStatusRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @Override
    public void hardDelete(Long id) {

        ManglikStatus entity = manglikStatusRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manglik Status not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        manglikStatusRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public ManglikStatusResponseDTO getById(Long id) {

        ManglikStatus entity = manglikStatusRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manglik Status not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<ManglikStatusResponseDTO> getAll() {

        return manglikStatusRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ManglikStatusResponseDTO> getDeleted() {

        return manglikStatusRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<ManglikStatusResponseDTO> getActive() {

        return manglikStatusRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ManglikStatusResponseDTO> getInactive() {

        return manglikStatusRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<ManglikStatusResponseDTO> getByAdmin(Long adminId) {

        return manglikStatusRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ManglikStatusResponseDTO> getActiveByAdmin(Long adminId) {

        return manglikStatusRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ManglikStatusResponseDTO> getInactiveByAdmin(Long adminId) {

        return manglikStatusRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<ManglikStatusResponseDTO> search(String keyword) {

        return manglikStatusRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ManglikStatusResponseDTO> searchByAdmin(Long adminId,
                                                        String keyword) {

        return manglikStatusRepository
                .findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
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

    private ManglikStatusResponseDTO mapToResponse(ManglikStatus entity) {

        return ManglikStatusResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}