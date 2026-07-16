package com.example.serviceimpl;

import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.BloodGroup;
import com.example.repository.AdminRepository;
import com.example.repository.BloodGroupRepository;
import com.example.service.BloodGroupService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BloodGroupServiceImpl implements BloodGroupService {

    private final BloodGroupRepository bloodGroupRepository;
    private final AdminRepository adminRepository;
    private final AuditHelper auditHelper;
    // ✅ Create
    @Override
    @Transactional
    public BloodGroup create(BloodGroup bloodGroup, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));
        if (bloodGroupRepository.existsByTypeIgnoreCaseAndDeletedAtIsNull(
                bloodGroup.getType())) {

            throw new BadRequestException("Blood group already exists");
        }
        bloodGroup.setAdmin(admin);

        BloodGroup saved = bloodGroupRepository.save(bloodGroup);

        auditHelper.logCreate(
                "MASTER_DATA",
                "BLOOD_GROUP",
                saved.getId(),
                saved.getType(),
                "Type=" + saved.getType()
                        + ", Active=" + saved.getIsActive()
        );

        return saved;    }

    // ✅ Get by ID
    @Override
    public BloodGroup getById(Long id, Long adminId) {

        BloodGroup bloodGroup = bloodGroupRepository.findById(id)
                .filter(bg -> bg.getDeletedAt() == null)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Blood group not found"));
        if (adminId != null
                && bloodGroup.getAdmin() != null
                && !bloodGroup.getAdmin().getId().equals(adminId)) {

            throw new BadRequestException("Unauthorized access");
        }

        return bloodGroup;
    }
    // ✅ Get all
    @Override
    public List<BloodGroup> getAll(Long adminId) {

        if (adminId == null) {
            return bloodGroupRepository.findAll()
                    .stream()
                    .filter(bg -> bg.getDeletedAt() == null)
                    .toList();
        }

        return bloodGroupRepository.findByAdminIdAndDeletedAtIsNull(adminId);
    }
    // ✅ Get active
    @Override
    public List<BloodGroup> getActive(Long adminId) {

        if (adminId == null) {

            return bloodGroupRepository.findAll()
                    .stream()
                    .filter(bg ->
                            bg.getDeletedAt() == null &&
                                    Boolean.TRUE.equals(bg.getIsActive()))
                    .toList();
        }

        return bloodGroupRepository.findByAdminIdAndIsActiveTrueAndDeletedAtIsNull(adminId);
    }
    // ✅ Update
    @Override
    @Transactional
    public BloodGroup update(Long id, BloodGroup updated, Long adminId) {

        BloodGroup existing = getById(id, adminId);
        String oldValue = "Type=" + existing.getType()
                + ", Active=" + existing.getIsActive();

        boolean wasActive = Boolean.TRUE.equals(existing.getIsActive());
        if (bloodGroupRepository.existsByTypeIgnoreCaseAndDeletedAtIsNull(
                updated.getType())
                && !existing.getType().equalsIgnoreCase(updated.getType())) {

            throw new BadRequestException("Blood group already exists");
        }
        existing.setType(updated.getType().trim());
        existing.setIsActive(updated.getIsActive());

        BloodGroup saved = bloodGroupRepository.save(existing);

        String newValue = "Type=" + saved.getType()
                + ", Active=" + saved.getIsActive();

        boolean isActive = Boolean.TRUE.equals(saved.getIsActive());

        auditHelper.logUpdate(
                "MASTER_DATA",
                "BLOOD_GROUP",
                saved.getId(),
                saved.getType(),
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

        BloodGroup bloodGroup = bloodGroupRepository.findById(id)
                .filter(bg -> bg.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Blood group not found"));

        String oldValue = "Type=" + bloodGroup.getType()
                + ", Active=" + bloodGroup.getIsActive();

        bloodGroup.setDeletedAt(LocalDateTime.now());
        bloodGroup.setDeletedBy(deletedBy);

        bloodGroupRepository.save(bloodGroup);

        auditHelper.logDelete(
                "MASTER_DATA",
                "BLOOD_GROUP",
                bloodGroup.getId(),
                bloodGroup.getType(),
                oldValue
        );
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {

        BloodGroup bloodGroup = bloodGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood group not found"));

        String oldValue = "Type=" + bloodGroup.getType()
                + ", Active=" + bloodGroup.getIsActive();

        bloodGroupRepository.delete(bloodGroup);

        auditHelper.logHardDelete(
                "MASTER_DATA",
                "BLOOD_GROUP",
                bloodGroup.getId(),
                bloodGroup.getType(),
                oldValue
        );
    }
    @Override
    @Transactional
    public BloodGroup restore(Long id) {

        BloodGroup bloodGroup = bloodGroupRepository.findById(id)
                .filter(bg -> bg.getDeletedAt() != null)
                .orElseThrow(() -> new ResourceNotFoundException("Deleted blood group not found"));

        bloodGroup.setDeletedAt(null);
        bloodGroup.setDeletedBy(null);

        BloodGroup restored = bloodGroupRepository.save(bloodGroup);

        auditHelper.logRestore(
                "MASTER_DATA",
                "BLOOD_GROUP",
                restored.getId(),
                restored.getType(),
                "Type=" + restored.getType()
                        + ", Active=" + restored.getIsActive()
        );

        return restored;
    }
    @Override
    @Transactional
    public List<BloodGroup> getDeleted(Long adminId) {

        return bloodGroupRepository.findAll()
                .stream()
                .filter(bg -> bg.getDeletedAt() != null)
                .toList();
    }
}