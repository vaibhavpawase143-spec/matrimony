package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.model.BloodGroup;
import com.example.repository.AdminRepository;
import com.example.repository.BloodGroupRepository;
import com.example.service.BloodGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodGroupServiceImpl implements BloodGroupService {

    private final BloodGroupRepository bloodGroupRepository;
    private final AdminRepository adminRepository;

    // ✅ Create
    @Override
    public BloodGroup create(BloodGroup bloodGroup, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 🔍 Duplicate check
        boolean exists = bloodGroupRepository.findAll().stream()
                .anyMatch(bg -> bg.getType().equalsIgnoreCase(bloodGroup.getType())
                        && bg.getAdmin().getId().equals(adminId));

        if (exists) {
            throw new RuntimeException("Blood group already exists");
        }

        bloodGroup.setAdmin(admin);

        return bloodGroupRepository.save(bloodGroup);
    }

    // ✅ Get by ID
    @Override
    public BloodGroup getById(Long id, Long adminId) {

        BloodGroup bg = bloodGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood group not found"));

        if (!bg.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return bg;
    }

    // ✅ Get all
    @Override
    public List<BloodGroup> getAll(Long adminId) {

        return bloodGroupRepository.findAll()
                .stream()
                .filter(bg -> bg.getAdmin().getId().equals(adminId))
                .toList();
    }

    // ✅ Get active
    @Override
    public List<BloodGroup> getActive(Long adminId) {

        return bloodGroupRepository.findAll()
                .stream()
                .filter(bg -> bg.getAdmin().getId().equals(adminId)
                        && Boolean.TRUE.equals(bg.getIsActive()))
                .toList();
    }

    // ✅ Update
    @Override
    public BloodGroup update(Long id, BloodGroup updated, Long adminId) {

        BloodGroup existing = getById(id, adminId);

        // 🔍 Duplicate check (exclude current record)
        boolean exists = bloodGroupRepository.findAll().stream()
                .anyMatch(bg -> bg.getType().equalsIgnoreCase(updated.getType())
                        && bg.getAdmin().getId().equals(adminId)
                        && !bg.getId().equals(id));

        if (exists) {
            throw new RuntimeException("Blood group already exists");
        }

        existing.setType(updated.getType());
        existing.setIsActive(updated.getIsActive());

        return bloodGroupRepository.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id, Long adminId) {

        BloodGroup bg = getById(id, adminId);
        bloodGroupRepository.delete(bg);
    }
}