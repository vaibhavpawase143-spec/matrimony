package com.example.serviceimpl;

import com.example.dto.request.RoleRequestDTO;
import com.example.dto.response.RoleResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.service.CurrentAdminService;
import com.example.service.RoleService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CurrentAdminService currentAdminService;
    private final AuditHelper auditHelper;

    private static final String MODULE = "Master";
    private static final String ENTITY = "Role";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public RoleResponseDTO create(RoleRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (roleRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                admin.getId())) {

            throw new BadRequestException("Role already exists.");
        }

        Role entity = Role.builder()
                .admin(admin)
                .name(requestDto.getName())
                .isActive(requestDto.getIsActive())
                .build();

        entity = roleRepository.save(entity);

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
    public RoleResponseDTO update(Long id,
                                  RoleRequestDTO requestDto) {

        Role entity = roleRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        if (!entity.getName().equalsIgnoreCase(requestDto.getName())
                && roleRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Role already exists.");
        }

        String oldValue = entity.getName();
        Boolean oldActive = entity.getIsActive();

        entity.setName(requestDto.getName());
        entity.setIsActive(requestDto.getIsActive());

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

        return mapToResponse(entity);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void softDelete(Long id) {

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
    }
    // =====================================================
    // RESTORE
    // =====================================================

    @Override
    public void restore(Long id) {

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
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @Override
    public void hardDelete(Long id) {

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
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public RoleResponseDTO getById(Long id) {

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
    public List<RoleResponseDTO> getAll() {

        return roleRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoleResponseDTO> getDeleted() {

        return roleRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @Override
    public List<RoleResponseDTO> getActive() {

        return roleRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoleResponseDTO> getInactive() {

        return roleRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @Override
    public List<RoleResponseDTO> getByAdmin(Long adminId) {

        return roleRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoleResponseDTO> getActiveByAdmin(Long adminId) {

        return roleRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoleResponseDTO> getInactiveByAdmin(Long adminId) {

        return roleRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    public List<RoleResponseDTO> search(String keyword) {

        return roleRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoleResponseDTO> searchByAdmin(Long adminId,
                                               String keyword) {

        return roleRepository
                .findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // JWT / SPRING SECURITY
    // =====================================================

    @Override
    public Role getRoleEntityByName(String name) {

        return roleRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found: " + name));
    }

    // =====================================================
    // DTO MAPPING
    // =====================================================

    private RoleResponseDTO mapToResponse(Role entity) {

        return RoleResponseDTO.builder()
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