package com.example.serviceimpl;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ReligionMapper;
import com.example.model.Admin;
import com.example.model.Religion;
import com.example.repository.AdminRepository;
import com.example.repository.ReligionRepository;
import com.example.service.ReligionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ReligionService with business logic for CRUD, soft delete, and search operations.
 * All queries exclude soft-deleted records by default.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReligionServiceImpl implements ReligionService {

    private final ReligionRepository religionRepository;
    private final AdminRepository adminRepository;

    // ================= CRUD OPERATIONS =================

    @Override
    @Transactional
    public ReligionResponseDTO create(ReligionRequestDTO dto, Long adminId) {
        log.debug("Creating new religion: {} for admin: {}", dto.getName(), adminId);

        // Validate admin exists
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    log.error("Admin not found: {}", adminId);
                    return new ResourceNotFoundException("Admin not found");
                });

        // Check for duplicates
        if (religionRepository.existsByNameIgnoreCaseAndAdmin(dto.getName(), adminId)) {
            log.warn("Religion already exists: {} for admin: {}", dto.getName(), adminId);
            throw new BadRequestException("Religion '" + dto.getName() + "' already exists for this admin");
        }

        // Create and save
        Religion religion = ReligionMapper.toEntity(dto, admin);
        Religion saved = religionRepository.save(religion);
        log.info("Religion created successfully with ID: {}", saved.getId());

        return ReligionMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReligionResponseDTO getById(Long id) {
        log.debug("Fetching religion by ID: {}", id);

        Religion religion = religionRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null) // Exclude soft-deleted
                .orElseThrow(() -> {
                    log.warn("Religion not found: {}", id);
                    return new ResourceNotFoundException("Religion not found");
                });

        return ReligionMapper.toResponseDTO(religion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> getAll() {
        log.debug("Fetching all non-deleted religions");
        return religionRepository.findAllActive()
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReligionResponseDTO update(Long id, ReligionRequestDTO dto) {
        log.debug("Updating religion ID: {}", id);

        Religion religion = religionRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Religion not found for update: {}", id);
                    return new ResourceNotFoundException("Religion not found");
                });

        // Check for name conflicts (if name is being changed)
        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(religion.getName())) {
            if (religionRepository.existsByNameIgnoreCaseAndAdmin(dto.getName(), religion.getAdmin().getId())) {
                log.warn("Religion name already exists: {} for admin: {}", dto.getName(), religion.getAdmin().getId());
                throw new BadRequestException("Religion '" + dto.getName() + "' already exists for this admin");
            }
        }

        ReligionMapper.updateEntity(religion, dto);
        Religion updated = religionRepository.save(religion);
        log.info("Religion updated successfully with ID: {}", updated.getId());

        return ReligionMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {
        log.debug("Soft deleting religion ID: {} by admin: {}", id, deletedBy);

        Religion religion = religionRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Religion not found for deletion: {}", id);
                    return new ResourceNotFoundException("Religion not found");
                });

        religion.setDeletedAt(LocalDateTime.now());
        religion.setDeletedBy(deletedBy);
        religionRepository.save(religion);
        log.info("Religion soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        log.warn("Hard deleting religion ID: {} - This is a permanent operation", id);

        if (!religionRepository.existsById(id)) {
            log.error("Religion not found for hard deletion: {}", id);
            throw new ResourceNotFoundException("Religion not found");
        }

        religionRepository.deleteById(id);
        log.info("Religion hard deleted successfully with ID: {}", id);
    }

    // ================= ADMIN-SPECIFIC QUERIES =================

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> getByAdmin(Long adminId) {
        log.debug("Fetching all religions for admin: {}", adminId);
        return religionRepository.findByAdminIdAndDeletedAtIsNull(adminId)
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> getActiveByAdmin(Long adminId) {
        log.debug("Fetching active religions for admin: {}", adminId);
        return religionRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId)
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> getInactiveByAdmin(Long adminId) {
        log.debug("Fetching inactive religions for admin: {}", adminId);
        return religionRepository.findByAdminIdAndIsActiveFalseAndDeletedAtIsNull(adminId)
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> searchByAdmin(Long adminId, String keyword) {
        log.debug("Searching religions for admin: {} with keyword: {}", adminId, keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return getByAdmin(adminId);
        }
        return religionRepository.searchByAdminAndKeyword(adminId, keyword)
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReligionResponseDTO> getDeletedByAdmin(Long adminId) {
        log.debug("Fetching deleted religions for admin: {}", adminId);
        return religionRepository.findByAdminIdAndDeletedAtIsNotNull(adminId)
                .stream()
                .map(ReligionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsForAdmin(String name, Long adminId) {
        log.debug("Checking if religion exists: {} for admin: {}", name, adminId);
        return religionRepository.existsByNameIgnoreCaseAndAdmin(name, adminId);
    }

    @Override
    @Transactional
    public ReligionResponseDTO restore(Long id) {
        log.debug("Restoring deleted religion ID: {}", id);

        Religion religion = religionRepository.findById(id)
                .filter(r -> r.getDeletedAt() != null)
                .orElseThrow(() -> {
                    log.warn("Deleted religion not found for restoration: {}", id);
                    return new ResourceNotFoundException("Deleted religion not found");
                });

        religion.setDeletedAt(null);
        religion.setDeletedBy(null);
        Religion restored = religionRepository.save(religion);
        log.info("Religion restored successfully with ID: {}", restored.getId());

        return ReligionMapper.toResponseDTO(restored);
    }
}