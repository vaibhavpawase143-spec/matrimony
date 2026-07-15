package com.example.serviceimpl;

import com.example.dto.request.HeightRequestDTO;
import com.example.dto.response.HeightResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Height;
import com.example.repository.AdminRepository;
import com.example.repository.HeightRepository;
import com.example.service.HeightService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HeightServiceImpl implements HeightService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "HEIGHT";

    private final HeightRepository heightRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public HeightResponseDTO create(HeightRequestDTO requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (heightRepository.existsByHeightIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getHeight(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Height already exists.");
        }

        Height entity = Height.builder()
                .admin(admin)
                .height(requestDto.getHeight().trim())
                .isActive(requestDto.getIsActive())
                .build();

        Height saved = heightRepository.save(entity);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                saved.getId(),
                saved.getHeight(),
                saved.getHeight()
        );

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public HeightResponseDTO update(Long id,
                                    HeightRequestDTO requestDto) {

        Height entity = heightRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Height not found."));

        boolean oldStatus = entity.getIsActive();

        if (heightRepository
                .findByHeightIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getHeight(),
                        requestDto.getAdminId())
                .filter(h -> !h.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Height already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = entity.getHeight();

        entity.setAdmin(admin);
        entity.setHeight(requestDto.getHeight().trim());
        entity.setIsActive(requestDto.getIsActive());

        Height updated = heightRepository.save(entity);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                updated.getId(),
                updated.getHeight(),
                oldValue,
                updated.getHeight(),
                oldStatus,
                updated.getIsActive()
        );

        return mapToResponse(updated);
    }

    // =========================
    // SOFT DELETE
    // =========================

    @Override
    public void softDelete(Long id) {

        Height entity = heightRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Height not found."));

        entity.setDeletedAt(LocalDateTime.now());

        heightRepository.save(entity);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getHeight(),
                entity.getHeight()
        );
    }

    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        Height entity = heightRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Height not found."));

        if (heightRepository.existsByHeightIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                entity.getHeight(),
                entity.getAdmin().getId())) {

            throw new BadRequestException("Height already exists.");
        }

        entity.setDeletedAt(null);
        entity.setDeletedBy(null);

        heightRepository.save(entity);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getHeight(),
                entity.getHeight()
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        Height entity = heightRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Height not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                entity.getId(),
                entity.getHeight(),
                entity.getHeight()
        );

        heightRepository.delete(entity);
    }

    // =========================
    // GET BY ID
    // =========================

    @Override
    public HeightResponseDTO getById(Long id) {

        return mapToResponse(
                heightRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Height not found."))
        );
    }

    // =========================
    // GET ALL
    // =========================

    @Override
    public List<HeightResponseDTO> getAll() {

        return heightRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HeightResponseDTO> getDeleted() {

        return heightRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @Override
    public List<HeightResponseDTO> getActive() {

        return heightRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HeightResponseDTO> getInactive() {

        return heightRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // ADMIN WISE
    // =========================

    @Override
    public List<HeightResponseDTO> getByAdmin(Long adminId) {

        return heightRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HeightResponseDTO> getActiveByAdmin(Long adminId) {

        return heightRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HeightResponseDTO> getInactiveByAdmin(Long adminId) {

        return heightRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // SEARCH
    // =========================

    @Override
    public List<HeightResponseDTO> search(String keyword) {

        return heightRepository
                .findByHeightContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HeightResponseDTO> searchByAdmin(Long adminId,
                                                 String keyword) {

        return heightRepository
                .findByAdmin_IdAndHeightContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // DTO MAPPING
    // =========================

    private HeightResponseDTO mapToResponse(Height entity) {

        return HeightResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getHeight())      // Frontend dropdown compatibility
                .height(entity.getHeight())
                .adminId(entity.getAdmin().getId())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}