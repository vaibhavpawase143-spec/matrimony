package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Complexion;
import com.example.repository.AdminRepository;
import com.example.repository.ComplexionRepository;
import com.example.service.ComplexionService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplexionServiceImpl implements ComplexionService {

    private final ComplexionRepository complexionRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // 🔍 Get all
    @Override
    @Transactional(readOnly = true)
    public List<Complexion> getAll() {

        return complexionRepository.findByDeletedAtIsNull();
    }
    // 🔍 Get active
    @Override
    @Transactional(readOnly = true)
    public List<Complexion> getActive() {

        return complexionRepository.findByIsActiveTrueAndDeletedAtIsNull();
    }

    // 🔍 Get by ID
    @Override
    @Transactional(readOnly = true)
    public Complexion getById(Long id) {

        return complexionRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complexion not found"));
    }

    // ✅ Create
    @Override
    @Transactional
    public Complexion create(Complexion complexion) {

        Admin admin = adminRepository.findById(
                complexion.getAdmin().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Admin not found"));

        complexion.setValue(complexion.getValue().trim());

        if (complexionRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                complexion.getValue())) {

            throw new BadRequestException("Complexion already exists");
        }

        complexion.setAdmin(admin);

        Complexion saved = complexionRepository.save(complexion);

        auditHelper.logCreate(
                "MASTER_DATA",
                "COMPLEXION",
                saved.getId(),
                saved.getValue(),
                "Value=" + saved.getValue()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }

    // 🔄 Update
    @Override
    @Transactional
    public Complexion update(Long id, Complexion updated) {

        Complexion existing = getById(id);

        String oldValue = "Value=" + existing.getValue()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        updated.setValue(updated.getValue().trim());

        if (!existing.getValue().equalsIgnoreCase(updated.getValue())
                && complexionRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                updated.getValue())) {

            throw new BadRequestException("Complexion already exists");
        }

        existing.setValue(updated.getValue());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        Complexion saved = complexionRepository.save(existing);

        String newValue = "Value=" + saved.getValue()
                + ", Active=" + saved.getIsActive();

        auditHelper.logUpdate(
                "MASTER_DATA",
                "COMPLEXION",
                saved.getId(),
                saved.getValue(),
                oldValue,
                newValue,
                wasActive,
                Boolean.TRUE.equals(saved.getIsActive())
        );

        return saved;
    }
    // ❌ Delete
    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        Complexion complexion = getById(id);

        String oldValue = "Value=" + complexion.getValue()
                + ", Active=" + complexion.getIsActive();

        complexion.setDeletedAt(LocalDateTime.now());
        complexion.setDeletedBy(deletedBy);

        complexionRepository.save(complexion);

        auditHelper.logDelete(
                "MASTER_DATA",
                "COMPLEXION",
                complexion.getId(),
                complexion.getValue(),
                oldValue
        );
    }

    // 🔍 Admin-based
    @Override
    @Transactional(readOnly = true)
    public List<Complexion> getByAdmin(Long adminId) {

        return complexionRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Complexion> getActiveByAdmin(Long adminId) {

        return complexionRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Complexion> search(String keyword) {

        return complexionRepository.findByValueContainingIgnoreCaseAndDeletedAtIsNull(
                keyword.trim()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public List<Complexion> getDeleted() {

        return complexionRepository.findByDeletedAtIsNotNull();
    }
    @Override
    @Transactional
    public Complexion restore(Long id) {

        Complexion complexion = complexionRepository.findById(id)
                .filter(c -> c.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted complexion not found"));

        complexion.setDeletedAt(null);
        complexion.setDeletedBy(null);
        complexion.setUpdatedAt(LocalDateTime.now());

        Complexion restored = complexionRepository.save(complexion);

        auditHelper.logRestore(
                "MASTER_DATA",
                "COMPLEXION",
                restored.getId(),
                restored.getValue(),
                "Value=" + restored.getValue()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional
    public void hardDelete(Long id) {

        Complexion complexion = complexionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complexion not found"));

        String oldValue = "Value=" + complexion.getValue()
                + ", Active=" + complexion.getIsActive();

        complexionRepository.delete(complexion);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "COMPLEXION",
                complexion.getId(),
                complexion.getValue(),
                oldValue
        );
    }


}