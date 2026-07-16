package com.example.serviceimpl;

import com.example.dto.request.EducationLevelRequestDto;
import com.example.dto.response.EducationLevelResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.EducationLevel;
import com.example.repository.AdminRepository;
import com.example.repository.EducationLevelRepository;
import com.example.service.EducationLevelService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationLevelServiceImpl implements EducationLevelService {

    private static final String MODULE = "MASTER";
    private static final String ENTITY = "EDUCATION_LEVEL";

    private final EducationLevelRepository educationLevelRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    @Override
    public EducationLevelResponseDto create(EducationLevelRequestDto requestDto) {

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        if (educationLevelRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                requestDto.getName(),
                requestDto.getAdminId())) {

            throw new BadRequestException("Education Level already exists.");
        }

        EducationLevel educationLevel = EducationLevel.builder()
                .admin(admin)
                .name(requestDto.getName().trim())
                .isActive(requestDto.getIsActive())
                .build();

        EducationLevel saved = educationLevelRepository.save(educationLevel);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                saved.getId(),
                saved.getName(),
                saved.getName()
        );

        return mapToResponse(saved);
    }

    @Override
    public EducationLevelResponseDto update(Long id,
                                            EducationLevelRequestDto requestDto) {

        EducationLevel educationLevel = educationLevelRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Education Level not found."));

        boolean oldStatus = educationLevel.getIsActive();

        if (educationLevelRepository
                .findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                        requestDto.getName(),
                        requestDto.getAdminId())
                .filter(e -> !e.getId().equals(id))
                .isPresent()) {

            throw new BadRequestException("Education Level already exists.");
        }

        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found."));

        String oldValue = educationLevel.getName();

        educationLevel.setAdmin(admin);
        educationLevel.setName(requestDto.getName().trim());
        educationLevel.setIsActive(requestDto.getIsActive());

        EducationLevel updated = educationLevelRepository.save(educationLevel);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                updated.getId(),
                updated.getName(),
                oldValue,
                updated.getName(),
                oldStatus,
                updated.getIsActive()
        );

        return mapToResponse(updated);
    }

    @Override
    public void softDelete(Long id) {

        EducationLevel educationLevel = educationLevelRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Education Level not found."));

        educationLevel.setDeletedAt(LocalDateTime.now());

        educationLevelRepository.save(educationLevel);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                educationLevel.getId(),
                educationLevel.getName(),
                educationLevel.getName()
        );
    }
    @Override
    public void restore(Long id) {

        EducationLevel educationLevel = educationLevelRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Education Level not found."));

        if (educationLevelRepository.existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
                educationLevel.getName(),
                educationLevel.getAdmin().getId())) {

            throw new BadRequestException("Education Level already exists.");
        }

        educationLevel.setDeletedAt(null);
        educationLevel.setDeletedBy(null);

        educationLevelRepository.save(educationLevel);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                educationLevel.getId(),
                educationLevel.getName(),
                educationLevel.getName()
        );
    }

    @Override
    public void hardDelete(Long id) {

        EducationLevel educationLevel = educationLevelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Education Level not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                educationLevel.getId(),
                educationLevel.getName(),
                educationLevel.getName()
        );

        educationLevelRepository.delete(educationLevel);
    }

    @Override
    public EducationLevelResponseDto getById(Long id) {

        return mapToResponse(
                educationLevelRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Education Level not found."))
        );
    }

    @Override
    public List<EducationLevelResponseDto> getAll() {

        return educationLevelRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getDeleted() {

        return educationLevelRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getActive() {

        return educationLevelRepository.findByIsActiveTrueAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getInactive() {

        return educationLevelRepository.findByIsActiveFalseAndDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getByAdmin(Long adminId) {

        return educationLevelRepository.findByAdmin_IdAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getActiveByAdmin(Long adminId) {

        return educationLevelRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> getInactiveByAdmin(Long adminId) {

        return educationLevelRepository
                .findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> search(String keyword) {

        return educationLevelRepository
                .findByNameContainingIgnoreCaseAndDeletedAtIsNull(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EducationLevelResponseDto> searchByAdmin(Long adminId, String keyword) {

        return educationLevelRepository
                .findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EducationLevelResponseDto mapToResponse(EducationLevel entity) {

        return EducationLevelResponseDto.builder()
                .id(entity.getId())
                .adminId(entity.getAdmin().getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}