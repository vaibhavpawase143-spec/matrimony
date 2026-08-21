package com.example.serviceimpl;

import com.example.dto.request.StateRequestDTO;
import com.example.dto.response.StateResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Country;
import com.example.model.State;
import com.example.repository.AdminRepository;
import com.example.repository.CountryRepository;
import com.example.repository.StateRepository;
import com.example.service.CurrentAdminService;
import com.example.service.StateService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final AdminRepository adminRepository;
    private final CountryRepository countryRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "State";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public StateResponseDTO create(StateRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        Country country = countryRepository.findById(requestDto.getCountryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        if (stateRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("State already exists.");
        }

        State entity = State.builder()
                .admin(admin)
                .country(country)
                .name(requestDto.getName().trim())
                .isActive(
                        requestDto.getIsActive() != null
                                ? requestDto.getIsActive()
                                : true
                )
                .build();

        entity = stateRepository.save(entity);

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
    public StateResponseDTO update(
            Long id,
            StateRequestDTO requestDto) {

        State entity = stateRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found."));

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        Country country = countryRepository.findById(requestDto.getCountryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Country not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && stateRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("State already exists.");
        }

        String oldName = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setAdmin(admin);
        entity.setCountry(country);
        entity.setName(requestDto.getName().trim());
        entity.setIsActive(requestDto.getIsActive());

        entity = stateRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                oldName,
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

        State entity = stateRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found."));

        String oldName = entity.getName();
        Boolean oldActive = entity.getIsActive();

        // Soft delete
        entity.setIsActive(false);
        entity.setDeletedAt(java.time.LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        stateRepository.save(entity);

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
    @Transactional
    public void restore(Long id) {

        State entity = stateRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted State not found."));

        String oldName = entity.getName();

        // Restore
        entity.setDeletedAt(null);
        entity.setDeletedBy(null);
        entity.setIsActive(true);

        stateRepository.save(entity);

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

        State entity = stateRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        stateRepository.delete(entity);
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public StateResponseDTO getById(Long id) {

        State entity = stateRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getAll() {

        return stateRepository.findAllWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getDeleted() {

        return stateRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getActive() {

        return stateRepository.findActiveWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getInactive() {

        return stateRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getByAdmin(Long adminId) {

        return stateRepository.findActiveByAdminWithRelations(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getActiveByAdmin(Long adminId) {

        return stateRepository
                .findActiveByAdminWithRelations(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getInactiveByAdmin(Long adminId) {

        return stateRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // COUNTRY
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getByCountryAndAdmin(
            Long countryId,
            Long adminId) {

        return stateRepository
                .findByCountryAndAdminWithRelations(
                        countryId,
                        adminId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getActiveByCountryAndAdmin(
            Long countryId,
            Long adminId) {

        return stateRepository
                .findByCountry_IdAndAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(
                        countryId,
                        adminId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> getInactiveByCountryAndAdmin(
            Long countryId,
            Long adminId) {

        return stateRepository
                .findByCountry_IdAndAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(
                        countryId,
                        adminId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> search(String keyword) {

        return stateRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDTO> searchByAdmin(
            Long adminId,
            String keyword) {

        return stateRepository
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

    private StateResponseDTO mapToResponse(State entity) {

        return StateResponseDTO.builder()
                .id(entity.getId())

                .adminId(
                        entity.getAdmin() != null
                                ? entity.getAdmin().getId()
                                : null
                )

                .adminName(
                        entity.getAdmin() != null
                                ? entity.getAdmin().getName()
                                : null
                )

                .countryId(
                        entity.getCountry() != null
                                ? entity.getCountry().getId()
                                : null
                )

                .countryName(
                        entity.getCountry() != null
                                ? entity.getCountry().getName()
                                : null
                )

                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}