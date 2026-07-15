package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Drinking;
import com.example.repository.AdminRepository;
import com.example.repository.DrinkingRepository;
import com.example.service.DrinkingService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrinkingServiceImpl implements DrinkingService {

    private final DrinkingRepository drinkingRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // ================= CREATE =================

    @Override
    @Transactional
    public Drinking create(Drinking drinking) {

        Admin admin = adminRepository.findById(
                drinking.getAdmin().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Admin not found"));

        drinking.setValue(drinking.getValue().trim());

        if (drinkingRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                drinking.getValue())) {

            throw new BadRequestException("Drinking already exists");
        }

        drinking.setAdmin(admin);

        Drinking saved = drinkingRepository.save(drinking);

        auditHelper.logCreate(
                "MASTER_DATA",
                "DRINKING",
                saved.getId(),
                saved.getValue(),
                "Value=" + saved.getValue()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }

    // ================= UPDATE =================

    @Override
    @Transactional
    public Drinking update(Long id, Drinking updated) {

        Drinking existing = getById(id);

        String oldValue = "Value=" + existing.getValue()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setValue(updated.getValue().trim());

        if (!existing.getValue().equalsIgnoreCase(updated.getValue())
                && drinkingRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                updated.getValue())) {

            throw new BadRequestException("Drinking already exists");
        }

        existing.setName(updated.getName());
        existing.setValue(updated.getValue());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        Drinking saved = drinkingRepository.save(existing);

        String newValue = "Value=" + saved.getValue()
                + ", Active=" + saved.getIsActive();

        auditHelper.logUpdate(
                "MASTER_DATA",
                "DRINKING",
                saved.getId(),
                saved.getValue(),
                oldValue,
                newValue,
                wasActive,
                Boolean.TRUE.equals(saved.getIsActive())
        );

        return saved;
    }
    // ================= DELETE =================

    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        Drinking drinking = getById(id);

        String oldValue = "Value=" + drinking.getValue()
                + ", Active=" + drinking.getIsActive();

        drinking.setDeletedAt(LocalDateTime.now());
        drinking.setDeletedBy(deletedBy);

        drinkingRepository.save(drinking);

        auditHelper.logDelete(
                "MASTER_DATA",
                "DRINKING",
                drinking.getId(),
                drinking.getValue(),
                oldValue
        );
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {

        Drinking drinking = drinkingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Drinking not found"));

        String oldValue = "Value=" + drinking.getValue()
                + ", Active=" + drinking.getIsActive();

        drinkingRepository.delete(drinking);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "DRINKING",
                drinking.getId(),
                drinking.getValue(),
                oldValue
        );
    }

    @Override
    @Transactional
    public Drinking restore(Long id) {

        Drinking drinking = drinkingRepository.findById(id)
                .filter(d -> d.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted drinking not found"));

        drinking.setDeletedAt(null);
        drinking.setDeletedBy(null);
        drinking.setUpdatedAt(LocalDateTime.now());

        Drinking restored = drinkingRepository.save(drinking);

        auditHelper.logRestore(
                "MASTER_DATA",
                "DRINKING",
                restored.getId(),
                restored.getValue(),
                "Value=" + restored.getValue()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }

    // ================= GET =================

    @Override
    @Transactional(readOnly = true)
    public Drinking getById(Long id) {

        return drinkingRepository.findById(id)
                .filter(d -> d.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Drinking not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getAll() {

        return drinkingRepository.findByDeletedAtIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getDeleted() {

        return drinkingRepository.findByDeletedAtIsNotNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getActive() {

        return drinkingRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getInactive() {

        return drinkingRepository.findByIsActiveFalseAndDeletedAtIsNull();
    }

    // ================= ADMIN =================

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getByAdmin(Long adminId) {

        return drinkingRepository.findByAdmin_IdAndDeletedAtIsNull(adminId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> getActiveByAdmin(Long adminId) {

        return drinkingRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }

    // ================= SEARCH =================

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> search(String keyword) {

        return drinkingRepository
                .findByValueContainingIgnoreCaseAndDeletedAtIsNull(
                        keyword.trim()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drinking> searchByAdmin(Long adminId, String keyword) {

        return drinkingRepository
                .findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword.trim()
                );
    }
}