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

import java.util.List;

@Service
@RequiredArgsConstructor
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
    public StateResponseDTO update(Long id,
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
    public void restore(Long id) {

        State entity = stateRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted State not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

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
    public List<StateResponseDTO> getAll() {

        return stateRepository.findAllWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
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
    public List<StateResponseDTO> getActive() {

        return stateRepository.findActiveWithRelations()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
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
    public List<StateResponseDTO> getByAdmin(Long adminId) {

        return stateRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StateResponseDTO> getActiveByAdmin(Long adminId) {

        return stateRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
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
    public List<StateResponseDTO> getByCountryAndAdmin(Long countryId,
                                                       Long adminId) {

        return stateRepository
                .findByCountry_IdAndAdmin_IdAndDeletedAtIsNull(countryId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StateResponseDTO> getActiveByCountryAndAdmin(Long countryId,
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
    public List<StateResponseDTO> getInactiveByCountryAndAdmin(Long countryId,
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
    public List<StateResponseDTO> search(String keyword) {

        return stateRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StateResponseDTO> searchByAdmin(Long adminId,
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
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .adminName(entity.getAdmin() != null ? entity.getAdmin().getName() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .countryName(entity.getCountry() != null ? entity.getCountry().getName() : null)
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}