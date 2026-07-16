package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.DisabilityStatus;
import com.example.repository.AdminRepository;
import com.example.repository.DisabilityStatusRepository;
import com.example.service.DisabilityStatusService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DisabilityStatusServiceImpl implements DisabilityStatusService {

    private final DisabilityStatusRepository disabilityStatusRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // ================= CREATE =================

    @Override
    @Transactional
    public DisabilityStatus create(DisabilityStatus disabilityStatus) {

        Admin admin = adminRepository.findById(
                disabilityStatus.getAdmin().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Admin not found"));

        disabilityStatus.setValue(disabilityStatus.getValue().trim());

        if (disabilityStatusRepository
                .existsByValueIgnoreCaseAndDeletedAtIsNull(
                        disabilityStatus.getValue())) {

            throw new BadRequestException("Disability status already exists");
        }

        disabilityStatus.setAdmin(admin);

        DisabilityStatus saved = disabilityStatusRepository.save(disabilityStatus);

        auditHelper.logCreate(
                "MASTER_DATA",
                "DISABILITY_STATUS",
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
    public DisabilityStatus update(Long id, DisabilityStatus updated) {

        DisabilityStatus existing = getById(id);

        String oldValue = "Value=" + existing.getValue()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setValue(updated.getValue().trim());

        if (!existing.getValue().equalsIgnoreCase(updated.getValue())
                && disabilityStatusRepository
                .existsByValueIgnoreCaseAndDeletedAtIsNull(updated.getValue())) {

            throw new BadRequestException("Disability status already exists");
        }

        existing.setValue(updated.getValue());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        DisabilityStatus saved = disabilityStatusRepository.save(existing);

        String newValue = "Value=" + saved.getValue()
                + ", Active=" + saved.getIsActive();

        auditHelper.logUpdate(
                "MASTER_DATA",
                "DISABILITY_STATUS",
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

        DisabilityStatus disabilityStatus = getById(id);

        String oldValue = "Value=" + disabilityStatus.getValue()
                + ", Active=" + disabilityStatus.getIsActive();

        disabilityStatus.setDeletedAt(LocalDateTime.now());
        disabilityStatus.setDeletedBy(deletedBy);

        disabilityStatusRepository.save(disabilityStatus);

        auditHelper.logDelete(
                "MASTER_DATA",
                "DISABILITY_STATUS",
                disabilityStatus.getId(),
                disabilityStatus.getValue(),
                oldValue
        );
    }

    // ================= HARD DELETE =================

    @Override
    @Transactional
    public void hardDelete(Long id) {

        DisabilityStatus disabilityStatus = disabilityStatusRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Disability status not found"));

        String oldValue = "Value=" + disabilityStatus.getValue()
                + ", Active=" + disabilityStatus.getIsActive();

        disabilityStatusRepository.delete(disabilityStatus);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "DISABILITY_STATUS",
                disabilityStatus.getId(),
                disabilityStatus.getValue(),
                oldValue
        );
    }

    // ================= RESTORE =================

    @Override
    @Transactional
    public DisabilityStatus restore(Long id) {

        DisabilityStatus disabilityStatus = disabilityStatusRepository.findById(id)
                .filter(d -> d.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted disability status not found"));

        disabilityStatus.setDeletedAt(null);
        disabilityStatus.setDeletedBy(null);
        disabilityStatus.setUpdatedAt(LocalDateTime.now());

        DisabilityStatus restored = disabilityStatusRepository.save(disabilityStatus);

        auditHelper.logRestore(
                "MASTER_DATA",
                "DISABILITY_STATUS",
                restored.getId(),
                restored.getValue(),
                "Value=" + restored.getValue()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }

    // ================= GET BY ID =================

    @Override
    @Transactional(readOnly = true)
    public DisabilityStatus getById(Long id) {

        return disabilityStatusRepository.findById(id)
                .filter(disabilityStatus -> disabilityStatus.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Disability status not found"));
    }

    // ================= GET ALL =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getAll() {

        return disabilityStatusRepository.findByDeletedAtIsNull();
    }

    // ================= GET DELETED =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getDeleted() {

        return disabilityStatusRepository.findByDeletedAtIsNotNull();
    }

    // ================= ACTIVE =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getActive() {

        return disabilityStatusRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }
    // ================= INACTIVE =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getInactive() {

        return disabilityStatusRepository.findByIsActiveFalseAndDeletedAtIsNull();
    }

    // ================= BY ADMIN =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getByAdmin(Long adminId) {

        return disabilityStatusRepository.findByAdmin_IdAndDeletedAtIsNull(adminId);
    }

    // ================= ACTIVE BY ADMIN =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> getActiveByAdmin(Long adminId) {

        return disabilityStatusRepository
                .findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }

    // ================= SEARCH =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> search(String keyword) {

        return disabilityStatusRepository
                .findByValueContainingIgnoreCaseAndDeletedAtIsNull(
                        keyword.trim()
                );
    }

    // ================= SEARCH BY ADMIN =================

    @Override
    @Transactional(readOnly = true)
    public List<DisabilityStatus> searchByAdmin(Long adminId, String keyword) {

        return disabilityStatusRepository
                .findByAdmin_IdAndValueContainingIgnoreCaseAndDeletedAtIsNull(
                        adminId,
                        keyword.trim()
                );
    }
}