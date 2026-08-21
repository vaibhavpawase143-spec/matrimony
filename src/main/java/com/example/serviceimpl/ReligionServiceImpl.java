package com.example.serviceimpl;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Religion;
import com.example.repository.ReligionRepository;
import com.example.service.CurrentAdminService;
import com.example.service.MasterDataCacheService;
import com.example.service.ReligionService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReligionServiceImpl implements ReligionService {

    private final ReligionRepository religionRepository;

    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;
    private final MasterDataCacheService masterDataCacheService;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Religion";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "master:religions:all",
                    "master:religions:active",
                    "master:religions:inactive"
            },
            allEntries = true
    )
    public ReligionResponseDTO create(ReligionRequestDTO requestDto) {
        Admin admin = currentAdminService.getCurrentAdmin();

        if (religionRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Religion already exists.");
        }

        Religion entity = Religion.builder()
                .admin(admin)
                .name(requestDto.getName())
                .isActive(requestDto.getIsActive())
                .build();

        entity = religionRepository.save(entity);
        masterDataCacheService.evictReligions();

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
    @CacheEvict(
            value = {
                    "master:religions:all",
                    "master:religions:active",
                    "master:religions:inactive",
                    "master:religion"
            },
            allEntries = true
    )
    public ReligionResponseDTO update(Long id,
                                      ReligionRequestDTO requestDto) {

        Religion entity = religionRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && religionRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Religion already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName());
        entity.setIsActive(requestDto.getIsActive());

        entity = religionRepository.save(entity);
        masterDataCacheService.evictReligions();

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
    @CacheEvict(
            value = {
                    "master:religions:all",
                    "master:religions:active",
                    "master:religions:inactive",
                    "master:religion"
            },
            allEntries = true
    )
    public void softDelete(Long id) {

        Religion entity = religionRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        religionRepository.save(entity);
        masterDataCacheService.evictReligions();

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
    @CacheEvict(
            value = {
                    "master:religions:all",
                    "master:religions:active",
                    "master:religions:inactive"
            },
            allEntries = true
    )
    public void restore(Long id) {

        Religion entity = religionRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Religion not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        religionRepository.save(entity);
        masterDataCacheService.evictReligions();

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
    @CacheEvict(
            value = {
                    "master:religions:all",
                    "master:religions:active",
                    "master:religions:inactive"
            },
            allEntries = true
    )
    public void hardDelete(Long id) {

        Religion entity = religionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        religionRepository.delete(entity);
        masterDataCacheService.evictReligions();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    @Cacheable(value = "master:religion", key = "#id")
    public ReligionResponseDTO getById(Long id) {

        Religion entity = religionRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Cacheable(value = "master:religions:all")
    public List<ReligionResponseDTO> getAll() {

        return religionRepository.findAllByDeletedAtIsNullOrderByIdAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReligionResponseDTO> getDeleted() {

        return religionRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    @Cacheable(value = "master:religions:active")
    public List<ReligionResponseDTO> getActive() {

        return religionRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "master:religions:inactive")
    public List<ReligionResponseDTO> getInactive() {

        return religionRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<ReligionResponseDTO> getByAdmin(Long adminId) {

        return religionRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReligionResponseDTO> getActiveByAdmin(Long adminId) {

        return religionRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReligionResponseDTO> getInactiveByAdmin(Long adminId) {

        return religionRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<ReligionResponseDTO> search(String keyword) {

        return religionRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReligionResponseDTO> searchByAdmin(Long adminId,
                                                   String keyword) {

        return religionRepository
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

    private ReligionResponseDTO mapToResponse(Religion entity) {

        return ReligionResponseDTO.builder()
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