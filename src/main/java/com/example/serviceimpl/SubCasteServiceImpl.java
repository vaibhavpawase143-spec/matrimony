package com.example.serviceimpl;

import com.example.dto.request.SubCasteRequestDTO;
import com.example.dto.response.SubCasteResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Caste;
import com.example.model.SubCaste;
import com.example.repository.AdminRepository;
import com.example.repository.CasteRepository;
import com.example.repository.SubCasteRepository;
import com.example.service.CurrentAdminService;
import com.example.service.SubCasteService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCasteServiceImpl implements SubCasteService {

    private final SubCasteRepository subCasteRepository;
    private final AdminRepository adminRepository;
    private final CasteRepository casteRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Sub Caste";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public SubCasteResponseDTO create(SubCasteRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        Caste caste = casteRepository.findById(requestDto.getCasteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Caste not found."));

        if (subCasteRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Sub Caste already exists.");
        }

        SubCaste entity = SubCaste.builder()
                .admin(admin)
                .caste(caste)
                .name(requestDto.getName().trim())
                .isActive(
                        requestDto.getIsActive() != null
                                ? requestDto.getIsActive()
                                : true
                )
                .build();

        entity = subCasteRepository.save(entity);

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
    public SubCasteResponseDTO update(Long id,
                                      SubCasteRequestDTO requestDto) {

        SubCaste entity = subCasteRepository
                .findByIdWithRelations(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sub Caste not found."));

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        Caste caste = casteRepository.findById(requestDto.getCasteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Caste not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && subCasteRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Sub Caste already exists.");
        }

        String oldName = entity.getName();
        Boolean oldStatus = entity.getIsActive();

        entity.setAdmin(admin);
        entity.setCaste(caste);
        entity.setName(requestDto.getName().trim());
        entity.setIsActive(requestDto.getIsActive());

        entity = subCasteRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                oldName,
                entity.getName(),
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

        SubCaste entity = subCasteRepository
                .findByIdWithRelations(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sub Caste not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        subCasteRepository.save(entity);

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

        SubCaste entity = subCasteRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Sub Caste not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        subCasteRepository.save(entity);

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

        SubCaste entity = subCasteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sub Caste not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        subCasteRepository.delete(entity);
    }
    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public SubCasteResponseDTO getById(Long id) {

        SubCaste entity = subCasteRepository
                .findByIdWithRelations(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sub Caste not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<SubCasteResponseDTO> getAll() {

        return subCasteRepository.findAllWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getDeleted() {

        return subCasteRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<SubCasteResponseDTO> getActive() {

        return subCasteRepository.findActiveWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getInactive() {

        return subCasteRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<SubCasteResponseDTO> getByAdmin(Long adminId) {

        return subCasteRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getActiveByAdmin(Long adminId) {

        return subCasteRepository
                .findActiveByAdminWithRelations(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getInactiveByAdmin(Long adminId) {

        return subCasteRepository
                .findInactiveByAdminWithRelations(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // CASTE
    // =====================================================

    @Override
    public List<SubCasteResponseDTO> getByCasteAndAdmin(Long casteId,
                                                        Long adminId) {

        return subCasteRepository
                .findByCasteAndAdminWithRelations(casteId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getActiveByCasteAndAdmin(Long casteId,
                                                              Long adminId) {

        return subCasteRepository
                .findActiveByCasteAndAdminWithRelations(casteId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> getInactiveByCasteAndAdmin(Long casteId,
                                                                Long adminId) {

        return subCasteRepository
                .findInactiveByCasteAndAdminWithRelations(casteId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<SubCasteResponseDTO> search(String keyword) {

        return subCasteRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SubCasteResponseDTO> searchByAdmin(Long adminId,
                                                   String keyword) {

        return subCasteRepository
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

    private SubCasteResponseDTO mapToResponse(SubCaste entity) {

        return SubCasteResponseDTO.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin() != null
                        ? entity.getAdmin().getId()
                        : null)
                .adminName(entity.getAdmin() != null
                        ? entity.getAdmin().getName()
                        : null)
                .casteId(entity.getCaste() != null
                        ? entity.getCaste().getId()
                        : null)
                .casteName(entity.getCaste() != null
                        ? entity.getCaste().getName()
                        : null)
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}