package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.BodyType;
import com.example.repository.AdminRepository;
import com.example.repository.BodyTypeRepository;
import com.example.service.BodyTypeService;
import com.example.util.AuditHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyTypeServiceImpl implements BodyTypeService {

    private final BodyTypeRepository bodyTypeRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;
    // ✅ Create
    @Override
    @Transactional
    public BodyType create(BodyType bodyType, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        if (bodyTypeRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                bodyType.getValue().trim())) {

            throw new BadRequestException("Body type already exists");
        }

        bodyType.setValue(bodyType.getValue().trim());
        bodyType.setAdmin(admin);

        BodyType saved = bodyTypeRepository.save(bodyType);

        auditHelper.logCreate(
                "MASTER_DATA",
                "BODY_TYPE",
                saved.getId(),
                saved.getValue(),
                "Value=" + saved.getValue()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }
    // ✅ Get by ID
    @Override
    public BodyType getById(Long id, Long adminId) {

        BodyType bodyType = bodyTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Body type not found"));

        if (adminId != null
                && bodyType.getAdmin() != null
                && !bodyType.getAdmin().getId().equals(adminId)) {

            throw new BadRequestException("Unauthorized access");
        }

        return bodyType;
    }
    // ✅ Get all
    @Override
    public List<BodyType> getAll(Long adminId) {

        if (adminId == null) {

            return bodyTypeRepository.findAll()
                    .stream()
                    .filter(bt -> bt.getDeletedAt() == null)
                    .toList();
        }

        return bodyTypeRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }
    // ✅ Get active
    @Override
    public List<BodyType> getActive(Long adminId) {

        if (adminId == null) {

            return bodyTypeRepository.findAll()
                    .stream()
                    .filter(bt ->
                            bt.getDeletedAt() == null
                                    && Boolean.TRUE.equals(bt.getIsActive()))
                    .toList();
        }

        return bodyTypeRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }
    // ✅ Get inactive


    // ✅ Update
    @Override
    @Transactional
    public BodyType update(Long id, BodyType updated, Long adminId) {

        BodyType existing = getById(id, adminId);

        String oldValue = "Value=" + existing.getValue()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        if (bodyTypeRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                updated.getValue().trim())
                && !existing.getValue().equalsIgnoreCase(updated.getValue().trim())) {

            throw new BadRequestException("Body type already exists");
        }

        existing.setValue(updated.getValue().trim());
        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );
        BodyType saved = bodyTypeRepository.save(existing);

        String newValue = "Value=" + saved.getValue()
                + ", Active=" + saved.getIsActive();

        boolean isActive = Boolean.TRUE.equals(saved.getIsActive());

        auditHelper.logUpdate(
                "MASTER_DATA",
                "BODY_TYPE",
                saved.getId(),
                saved.getValue(),
                oldValue,
                newValue,
                wasActive,
                isActive
        );

        return saved;
    }
    // ✅ Delete
    @Override
    @Transactional
    public void delete(Long id, Long deletedBy) {

        BodyType bodyType = bodyTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Body type not found"));

        String oldValue = "Value=" + bodyType.getValue()
                + ", Active=" + bodyType.getIsActive();

        bodyType.setDeletedAt(LocalDateTime.now());
        bodyType.setDeletedBy(deletedBy);

        bodyTypeRepository.save(bodyType);

        auditHelper.logDelete(
                "MASTER_DATA",
                "BODY_TYPE",
                bodyType.getId(),
                bodyType.getValue(),
                oldValue
        );
    }
    @Override
    @Transactional
    public void hardDelete(Long id) {

        BodyType bodyType = bodyTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Body type not found"));

        String oldValue = "Value=" + bodyType.getValue()
                + ", Active=" + bodyType.getIsActive();

        bodyTypeRepository.delete(bodyType);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "BODY_TYPE",
                bodyType.getId(),
                bodyType.getValue(),
                oldValue
        );
    }
    @Override
    @Transactional
    public BodyType restore(Long id) {

        BodyType bodyType = bodyTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted body type not found"));

        bodyType.setDeletedAt(null);
        bodyType.setDeletedBy(null);
        bodyType.setUpdatedAt(LocalDateTime.now());
        BodyType restored = bodyTypeRepository.save(bodyType);

        auditHelper.logRestore(
                "MASTER_DATA",
                "BODY_TYPE",
                restored.getId(),
                restored.getValue(),
                "Value=" + restored.getValue()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional
    public List<BodyType> getDeleted(Long adminId) {

        return bodyTypeRepository.findAll()
                .stream()
                .filter(bt -> bt.getDeletedAt() != null)
                .toList();
    }
}