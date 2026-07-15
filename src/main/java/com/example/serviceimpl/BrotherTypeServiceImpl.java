package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.BrotherType;
import com.example.repository.AdminRepository;
import com.example.repository.BrotherTypeRepository;
import com.example.service.BrotherTypeService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrotherTypeServiceImpl implements BrotherTypeService {

    private final BrotherTypeRepository brotherTypeRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;

    // ✅ Get all (by admin)
    @Override
    public List<BrotherType> getAll(Long adminId) {

        if (adminId == null) {

            return brotherTypeRepository.findAll()
                    .stream()
                    .filter(bt -> bt.getDeletedAt() == null)
                    .toList();
        }

        return brotherTypeRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }

    // ✅ Get active (by admin)
    @Override
    public List<BrotherType> getActive(Long adminId) {

        if (adminId == null) {

            return brotherTypeRepository.findAll()
                    .stream()
                    .filter(bt ->
                            bt.getDeletedAt() == null
                                    && Boolean.TRUE.equals(bt.getIsActive()))
                    .toList();
        }

        return brotherTypeRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }
    // ✅ Get by ID (secure)
    @Override
    public BrotherType getById(Long id, Long adminId) {

        BrotherType brotherType = brotherTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brother type not found"));

        if (adminId != null
                && brotherType.getAdmin() != null
                && !brotherType.getAdmin().getId().equals(adminId)) {

            throw new BadRequestException("Unauthorized access");
        }

        return brotherType;
    }
    // ✅ Create
    @Override
   @Transactional
    public BrotherType create(BrotherType brotherType, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        if (brotherTypeRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                brotherType.getValue().trim())) {

            throw new BadRequestException("Brother type already exists");
        }

        brotherType.setValue(brotherType.getValue().trim());
        brotherType.setAdmin(admin);

        BrotherType saved = brotherTypeRepository.save(brotherType);

        auditHelper.logCreate(
                "MASTER_DATA",
                "BROTHER_TYPE",
                saved.getId(),
                saved.getValue(),
                "Value=" + saved.getValue()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;
    }
    // ✅ Update
    @Override
    @Transactional
    public BrotherType update(Long id, BrotherType updated, Long adminId) {

        BrotherType existing = getById(id, adminId);

        String oldValue = "Value=" + existing.getValue()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());

        if (brotherTypeRepository.existsByValueIgnoreCaseAndDeletedAtIsNull(
                updated.getValue().trim())
                && !existing.getValue().equalsIgnoreCase(updated.getValue().trim())) {

            throw new BadRequestException("Brother type already exists");
        }

        existing.setValue(updated.getValue().trim());

        existing.setIsActive(
                updated.getIsActive() != null
                        ? updated.getIsActive()
                        : existing.getIsActive()
        );

        BrotherType saved = brotherTypeRepository.save(existing);

        String newValue = "Value=" + saved.getValue()
                + ", Active=" + saved.getIsActive();

        boolean isActive = Boolean.TRUE.equals(saved.getIsActive());

        auditHelper.logUpdate(
                "MASTER_DATA",
                "BROTHER_TYPE",
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

        BrotherType brotherType = brotherTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brother type not found"));

        String oldValue = "Value=" + brotherType.getValue()
                + ", Active=" + brotherType.getIsActive();

        brotherType.setDeletedAt(LocalDateTime.now());
        brotherType.setDeletedBy(deletedBy);

        brotherTypeRepository.save(brotherType);

        auditHelper.logDelete(
                "MASTER_DATA",
                "BROTHER_TYPE",
                brotherType.getId(),
                brotherType.getValue(),
                oldValue
        );
    }
    // ✅ Search (by admin)
    @Override
    @Transactional
    public void hardDelete(Long id) {

        BrotherType brotherType = brotherTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brother type not found"));

        String oldValue = "Value=" + brotherType.getValue()
                + ", Active=" + brotherType.getIsActive();

        brotherTypeRepository.delete(brotherType);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "BROTHER_TYPE",
                brotherType.getId(),
                brotherType.getValue(),
                oldValue
        );
    }
    @Override
    @Transactional
    public BrotherType restore(Long id) {

        BrotherType brotherType = brotherTypeRepository.findById(id)
                .filter(bt -> bt.getDeletedAt() != null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted brother type not found"));

        brotherType.setDeletedAt(null);
        brotherType.setDeletedBy(null);
        brotherType.setUpdatedAt(LocalDateTime.now());

        BrotherType restored = brotherTypeRepository.save(brotherType);

        auditHelper.logRestore(
                "MASTER_DATA",
                "BROTHER_TYPE",
                restored.getId(),
                restored.getValue(),
                "Value=" + restored.getValue()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional
    public List<BrotherType> getDeleted(Long adminId) {

        return brotherTypeRepository.findByAdminIdAndDeletedAtIsNotNull(adminId);
    }
}