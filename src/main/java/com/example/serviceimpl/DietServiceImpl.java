package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Diet;
import com.example.repository.AdminRepository;
import com.example.repository.DietRepository;
import com.example.service.DietService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;


    // ✅ Create
    @Override
    @Transactional
    public Diet create(Diet diet) {

        Admin admin = adminRepository.findById(
                diet.getAdmin().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Admin not found"));

        diet.setName(diet.getName().trim());

        if (dietRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                diet.getName())) {

            throw new BadRequestException("Diet already exists");
        }

        diet.setAdmin(admin);

        Diet saved = dietRepository.save(diet);

        auditHelper.logCreate(
                "MASTER_DATA",
                "DIET",
                saved.getId(),
                saved.getName(),
                "Name=" + saved.getName()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }
    // 🔄 Update

    // ❌ Delete
    @Override
    @Transactional
    public Diet update(Long id, Diet updated) {

        Diet existing = getById(id);

        String oldValue = "Name=" + existing.getName()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setName(updated.getName().trim());

        if (!existing.getName().equalsIgnoreCase(updated.getName())
                && dietRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(
                updated.getName())) {

            throw new BadRequestException("Diet already exists");
        }

        existing.setName(updated.getName());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        Diet saved = dietRepository.save(existing);

        String newValue = "Name=" + saved.getName()
                + ", Active=" + saved.getIsActive();

        auditHelper.logUpdate(
                "MASTER_DATA",
                "DIET",
                saved.getId(),
                saved.getName(),
                oldValue,
                newValue,
                wasActive,
                Boolean.TRUE.equals(saved.getIsActive())
        );

        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        Diet diet = getById(id);

        String oldValue = "Name=" + diet.getName()
                + ", Active=" + diet.getIsActive();

        diet.setDeletedAt(LocalDateTime.now());
        diet.setDeletedBy(deletedBy);

        dietRepository.save(diet);

        auditHelper.logDelete(
                "MASTER_DATA",
                "DIET",
                diet.getId(),
                diet.getName(),
                oldValue
        );
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {

        Diet diet = dietRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Diet not found"));

        String oldValue = "Name=" + diet.getName()
                + ", Active=" + diet.getIsActive();

        dietRepository.delete(diet);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "DIET",
                diet.getId(),
                diet.getName(),
                oldValue
        );
    }
    @Override
    @Transactional
    public Diet restore(Long id) {

        Diet diet = dietRepository.findById(id)
                .filter(d -> d.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted diet not found"));

        diet.setDeletedAt(null);
        diet.setDeletedBy(null);
        diet.setUpdatedAt(LocalDateTime.now());

        Diet restored = dietRepository.save(diet);

        auditHelper.logRestore(
                "MASTER_DATA",
                "DIET",
                restored.getId(),
                restored.getName(),
                "Name=" + restored.getName()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getDeleted() {

        return dietRepository.findByDeletedAtIsNotNull();
    }

    // 🔍 Get by ID
    @Override
    @Transactional(readOnly = true)
    public Diet getById(Long id) {

        return dietRepository.findById(id)
                .filter(diet -> diet.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Diet not found"));
    }
    // 🔍 Get all
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getAll() {

        return dietRepository.findByDeletedAtIsNull();
    }
    // 🔍 Find by name

    // 🔍 Active / Inactive
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getActive() {

        return dietRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getInactive() {

        return dietRepository.findByIsActiveFalseAndDeletedAtIsNull();
    }

    // 🔍 Admin-based
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getByAdmin(Long adminId) {

        return dietRepository.findByAdmin_IdAndDeletedAtIsNull(adminId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Diet> getActiveByAdmin(Long adminId) {

        return dietRepository.findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }
    // 🔍 Search
    @Override
    @Transactional(readOnly = true)
    public List<Diet> search(String keyword) {

        return dietRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(
                keyword.trim()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<Diet> searchByAdmin(Long adminId, String keyword) {

        return dietRepository.findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                adminId,
                keyword.trim()
        );
    }
}