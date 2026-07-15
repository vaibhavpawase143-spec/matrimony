package com.example.serviceimpl;

import com.example.dto.request.MotherTongueRequestDTO;
import com.example.dto.response.MotherTongueResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.MotherTongue;
import com.example.repository.AdminRepository;
import com.example.repository.MotherTongueRepository;
import com.example.service.CurrentAdminService;
import com.example.service.MotherTongueService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MotherTongueServiceImpl implements MotherTongueService {

    private final MotherTongueRepository motherTongueRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Mother Tongue";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public MotherTongueResponseDTO create(MotherTongueRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (motherTongueRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Mother Tongue already exists.");
        }

        MotherTongue entity = MotherTongue.builder()
                .admin(admin)
                .name(requestDto.getName())
                .isActive(requestDto.getIsActive())
                .build();

        entity = motherTongueRepository.save(entity);

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
    public MotherTongueResponseDTO update(Long id,
                                          MotherTongueRequestDTO requestDto) {

        MotherTongue entity = motherTongueRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mother Tongue not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && motherTongueRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Mother Tongue already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName());
        entity.setIsActive(requestDto.getIsActive());

        entity = motherTongueRepository.save(entity);

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

        MotherTongue entity = motherTongueRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mother Tongue not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        motherTongueRepository.save(entity);

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

        MotherTongue entity = motherTongueRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Mother Tongue not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        motherTongueRepository.save(entity);

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

        MotherTongue entity = motherTongueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mother Tongue not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        motherTongueRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public MotherTongueResponseDTO getById(Long id) {

        MotherTongue entity = motherTongueRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mother Tongue not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<MotherTongueResponseDTO> getAll() {

        return motherTongueRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MotherTongueResponseDTO> getDeleted() {

        return motherTongueRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<MotherTongueResponseDTO> getActive() {

        return motherTongueRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MotherTongueResponseDTO> getInactive() {

        return motherTongueRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<MotherTongueResponseDTO> getByAdmin(Long adminId) {

        return motherTongueRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MotherTongueResponseDTO> getActiveByAdmin(Long adminId) {

        return motherTongueRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MotherTongueResponseDTO> getInactiveByAdmin(Long adminId) {

        return motherTongueRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<MotherTongueResponseDTO> search(String keyword) {

        return motherTongueRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MotherTongueResponseDTO> searchByAdmin(Long adminId,
                                                       String keyword) {

        return motherTongueRepository
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

    private MotherTongueResponseDTO mapToResponse(MotherTongue entity) {

        return MotherTongueResponseDTO.builder()
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