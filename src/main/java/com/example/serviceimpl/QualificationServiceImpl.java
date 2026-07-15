package com.example.serviceimpl;

import com.example.dto.request.QualificationRequestDTO;
import com.example.dto.response.QualificationResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Qualification;
import com.example.repository.AdminRepository;
import com.example.repository.QualificationRepository;
import com.example.service.CurrentAdminService;
import com.example.service.QualificationService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Qualification";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public QualificationResponseDTO create(QualificationRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (qualificationRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Qualification already exists.");
        }

        Qualification entity = Qualification.builder()
                .admin(admin)
                .name(requestDto.getName())
                .isActive(requestDto.getIsActive())
                .build();

        entity = qualificationRepository.save(entity);

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
    public QualificationResponseDTO update(Long id,
                                           QualificationRequestDTO requestDto) {

        Qualification entity = qualificationRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Qualification not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && qualificationRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Qualification already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName());
        entity.setIsActive(requestDto.getIsActive());

        entity = qualificationRepository.save(entity);

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

        Qualification entity = qualificationRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Qualification not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        qualificationRepository.save(entity);

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

        Qualification entity = qualificationRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Qualification not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        qualificationRepository.save(entity);

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

        Qualification entity = qualificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Qualification not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        qualificationRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public QualificationResponseDTO getById(Long id) {

        Qualification entity = qualificationRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Qualification not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<QualificationResponseDTO> getAll() {

        return qualificationRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QualificationResponseDTO> getDeleted() {

        return qualificationRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<QualificationResponseDTO> getActive() {

        return qualificationRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QualificationResponseDTO> getInactive() {

        return qualificationRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<QualificationResponseDTO> getByAdmin(Long adminId) {

        return qualificationRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QualificationResponseDTO> getActiveByAdmin(Long adminId) {

        return qualificationRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QualificationResponseDTO> getInactiveByAdmin(Long adminId) {

        return qualificationRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<QualificationResponseDTO> search(String keyword) {

        return qualificationRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QualificationResponseDTO> searchByAdmin(Long adminId,
                                                        String keyword) {

        return qualificationRepository
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

    private QualificationResponseDTO mapToResponse(Qualification entity) {

        return QualificationResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .adminName(null) // Avoid LazyInitializationException
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}