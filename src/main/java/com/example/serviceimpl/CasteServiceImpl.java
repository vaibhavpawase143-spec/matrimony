package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Caste;
import com.example.model.Religion;
import com.example.repository.AdminRepository;
import com.example.repository.CasteRepository;
import com.example.repository.ReligionRepository;
import com.example.service.CasteService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class CasteServiceImpl implements CasteService {

    private final CasteRepository casteRepository;
    private final AdminRepository adminRepository;
    private final ReligionRepository religionRepository;
    private final AuditHelper auditHelper;

    // =========================================
    // GET ALL
    // =========================================

    @Override
    public List<Caste> getAll(Long adminId) {

        List<Caste> list = casteRepository.findAllAvailable(adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }

    // =========================================
    // GET ACTIVE
    // =========================================

    @Override
    public List<Caste> getActive(Long adminId) {

        List<Caste> list = casteRepository.findAllActiveAvailable(adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }
    // =========================================
    // GET BY ID
    // =========================================

    @Override
    public Caste getById(Long id, Long adminId) {

        Caste caste = casteRepository.findAccessibleById(id, adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Caste not found"));

        if (caste.getReligion() != null) {
            caste.getReligion().getName();
        }

        return caste;
    }
    // =========================================
    // CREATE
    // =========================================

    @Override
    @Transactional
    public Caste create(Caste caste, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        Religion religion = religionRepository.findById(caste.getReligion().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found"));

        caste.setName(caste.getName().trim());

        if (casteRepository.existsByNameIgnoreCaseAndReligionIdAndDeletedAtIsNull(
                caste.getName(),
                religion.getId())) {

            throw new BadRequestException("Caste already exists");
        }

        caste.setAdmin(admin);
        caste.setReligion(religion);

        Caste saved = casteRepository.save(caste);

        auditHelper.logCreate(
                "MASTER_DATA",
                "CASTE",
                saved.getId(),
                saved.getName(),
                "Name=" + saved.getName()
                        + ", Religion=" + religion.getName()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }
    // =========================================
    // UPDATE
    // =========================================

    @Override
    @Transactional
    public Caste update(Long id, Caste updated, Long adminId) {

        Caste existing = getById(id, adminId);

        Religion religion = religionRepository.findById(updated.getReligion().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Religion not found"));

        String oldValue = "Name=" + existing.getName()
                + ", Religion=" + existing.getReligion().getName()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setName(updated.getName().trim());

        if (!existing.getName().equalsIgnoreCase(updated.getName())
                || !existing.getReligion().getId().equals(religion.getId())) {

            if (casteRepository.existsByNameIgnoreCaseAndReligionIdAndDeletedAtIsNull(
                    updated.getName(),
                    religion.getId())) {

                throw new BadRequestException("Caste already exists");
            }
        }

        existing.setName(updated.getName());
        existing.setReligion(religion);

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        Caste saved = casteRepository.save(existing);

        String newValue = "Name=" + saved.getName()
                + ", Religion=" + saved.getReligion().getName()
                + ", Active=" + saved.getIsActive();

        boolean isActive = Boolean.TRUE.equals(saved.getIsActive());

        auditHelper.logUpdate(
                "MASTER_DATA",
                "CASTE",
                saved.getId(),
                saved.getName(),
                oldValue,
                newValue,
                wasActive,
                isActive
        );

        return saved;
    }
    // =========================================
    // DELETE
    // =========================================
    @Override
    @Transactional
    public void hardDelete(Long id) {

        Caste caste = casteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Caste not found"));

        String oldValue = "Name=" + caste.getName()
                + ", Religion=" + caste.getReligion().getName()
                + ", Active=" + caste.getIsActive();

        casteRepository.delete(caste);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "CASTE",
                caste.getId(),
                caste.getName(),
                oldValue
        );
    }

    // GET BY RELIGION
    // =========================================

    @Override
    @Transactional(readOnly = true)
    public List<Caste> getByReligion(Long religionId, Long adminId) {

        List<Caste> list = casteRepository.findAvailableByReligion(
                religionId,
                adminId
        );

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }
    // =========================================
    // GET ACTIVE BY RELIGION
    // =========================================

    @Override
    @Transactional(readOnly = true)
    public List<Caste> getActiveByReligion(
            Long religionId,
            Long adminId
    ) {

        List<Caste> list = casteRepository.findActiveAvailableByReligion(
                religionId,
                adminId
        );

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }
    // =========================================
    // SEARCH
    // =========================================

    @Override
    @Transactional(readOnly = true)
    public List<Caste> search(String keyword, Long adminId) {

        List<Caste> list = casteRepository.searchAvailable(
                keyword.trim(),
                adminId
        );

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }
    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Caste not found"));

        String oldValue = "Name=" + caste.getName()
                + ", Religion=" + caste.getReligion().getName()
                + ", Active=" + caste.getIsActive();

        caste.setDeletedAt(LocalDateTime.now());
        caste.setDeletedBy(deletedBy);

        casteRepository.save(caste);

        auditHelper.logDelete(
                "MASTER_DATA",
                "CASTE",
                caste.getId(),
                caste.getName(),
                oldValue
        );
    }
    @Override
    @Transactional
    public Caste restore(Long id) {

        Caste caste = casteRepository.findById(id)
                .filter(c -> c.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted caste not found"));

        caste.setDeletedAt(null);
        caste.setDeletedBy(null);
        caste.setUpdatedAt(LocalDateTime.now());

        Caste restored = casteRepository.save(caste);

        auditHelper.logRestore(
                "MASTER_DATA",
                "CASTE",
                restored.getId(),
                restored.getName(),
                "Name=" + restored.getName()
                        + ", Religion=" + restored.getReligion().getName()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Caste> getDeleted(Long adminId) {

        return casteRepository.findByAdminIdAndDeletedAtIsNotNull(adminId);
    }

}