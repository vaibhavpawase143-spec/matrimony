package com.example.serviceimpl;

import com.example.dto.request.RoleRequestDTO;
import com.example.dto.response.RoleResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Role;
import com.example.repository.RoleRepository;
import com.example.service.CurrentAdminService;
import com.example.service.RoleService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Role";

    // =====================================================
    // CREATE ROLE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "roles",
                    "activeRoles",
                    "deletedRoles"
            },
            allEntries = true
    )
    public RoleResponseDTO create(RoleRequestDTO requestDto) {

        log.info("Creating new role: {}", requestDto.getName());

        if (roleRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                requestDto.getName())) {

            throw new BadRequestException("Role already exists.");
        }

        Role entity = Role.builder()
                .name(requestDto.getName().trim())
                .isActive(
                        requestDto.getIsActive() == null
                                ? Boolean.TRUE
                                : requestDto.getIsActive()
                )
                .build();

        entity = roleRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        log.info(
                "Role created successfully. Id={}, Name={}",
                entity.getId(),
                entity.getName()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // UPDATE ROLE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "roles",
                    "activeRoles",
                    "deletedRoles"
            },
            allEntries = true
    )
    public RoleResponseDTO update(Long id,
                                  RoleRequestDTO requestDto) {

        log.info("Updating role with id={}", id);

        Role entity = roleRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && roleRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                requestDto.getName())) {

            throw new BadRequestException("Role already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName().trim());

        if (requestDto.getIsActive() != null) {
            entity.setIsActive(requestDto.getIsActive());
        }

        entity = roleRepository.save(entity);

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

        log.info(
                "Role updated successfully. Id={}",
                entity.getId()
        );

        return mapToResponse(entity);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "roles",
                    "activeRoles",
                    "deletedRoles"
            },
            allEntries = true
    )
    public void softDelete(Long id) {

        log.info("Soft deleting role id={}", id);

        Role entity = roleRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(currentAdminService.getCurrentAdmin().getId());

        roleRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        log.info(
                "Role soft deleted successfully. Id={}",
                entity.getId()
        );
    }
    // =====================================================
    // RESTORE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "roles",
                    "activeRoles",
                    "deletedRoles"
            },
            allEntries = true
    )
    public void restore(Long id) {

        log.info("Restoring role id={}", id);

        Role entity = roleRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Role not found."));

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        roleRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        log.info("Role restored successfully. Id={}", entity.getId());
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @Override
    @CacheEvict(
            value = {
                    "roles",
                    "activeRoles",
                    "deletedRoles"
            },
            allEntries = true
    )
    public void hardDelete(Long id) {

        log.warn("Hard deleting role id={}", id);

        Role entity = roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getName(),
                entity.getName()
        );

        roleRepository.delete(entity);

        log.warn("Role permanently deleted. Id={}", entity.getId());
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "roles", key = "#id")
    public RoleResponseDTO getById(Long id) {

        log.debug("Fetching role by id={}", id);

        Role entity = roleRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        return mapToResponse(entity);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    @Cacheable("roles")
    public List<RoleResponseDTO> getAll() {

        log.debug("Fetching all roles");

        return roleRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    @Cacheable("deletedRoles")
    public List<RoleResponseDTO> getDeleted() {

        log.debug("Fetching deleted roles");

        return roleRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET ACTIVE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    @Cacheable("activeRoles")
    public List<RoleResponseDTO> getActive() {

        log.debug("Fetching active roles");

        return roleRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET INACTIVE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getInactive() {

        log.debug("Fetching inactive roles");

        return roleRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> search(String keyword) {

        log.debug("Searching roles using keyword={}", keyword);

        return roleRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    // =====================================================
    // JWT / SPRING SECURITY
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "roleByName", key = "#name")
    public Role getRoleEntityByName(String name) {

        log.debug("Fetching role by name={}", name);

        return roleRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + name
                        ));
    }

    // =====================================================
    // DTO MAPPING
    // =====================================================

    private RoleResponseDTO mapToResponse(Role entity) {

        return RoleResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }

}
