package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.model.Caste;
import com.example.model.Religion;
import com.example.repository.AdminRepository;
import com.example.repository.CasteRepository;
import com.example.repository.ReligionRepository;
import com.example.service.CasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CasteServiceImpl implements CasteService {

    private final CasteRepository repo;
    private final AdminRepository adminRepository;
    private final ReligionRepository religionRepository;

    // ✅ Get all (admin-based)
    @Override
    public List<Caste> getAll(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active (admin-based)
    @Override
    public List<Caste> getActive(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Get by ID (secure)
    @Override
    public Caste getById(Long id, Long adminId) {
        return repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));
    }

    // ✅ Create
    @Override
    public Caste create(Caste caste, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Religion religion = religionRepository.findById(caste.getReligion().getId())
                .orElseThrow(() -> new RuntimeException("Religion not found"));

        // 🔍 Duplicate check (name + religion + admin)
        if (repo.existsByNameIgnoreCaseAndReligionIdAndAdminId(
                caste.getName(), religion.getId(), adminId)) {
            throw new RuntimeException("Caste already exists");
        }

        caste.setAdmin(admin);
        caste.setReligion(religion);
        caste.setIsActive(true);

        return repo.save(caste);
    }

    // ✅ Update
    @Override
    public Caste update(Long id, Caste updated, Long adminId) {

        Caste existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        // 🔍 Duplicate check
        if (!existing.getName().equalsIgnoreCase(updated.getName()) &&
                repo.existsByNameIgnoreCaseAndReligionIdAndAdminId(
                        updated.getName(),
                        updated.getReligion().getId(),
                        adminId)) {
            throw new RuntimeException("Caste already exists");
        }

        existing.setName(updated.getName());
        existing.setReligion(updated.getReligion());
        existing.setIsActive(updated.getIsActive());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id, Long adminId) {

        Caste existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        repo.delete(existing);
    }

    // ✅ Get by religion (admin-safe)
    @Override
    public List<Caste> getByReligion(Long religionId, Long adminId) {
        return repo.findByReligionIdAndAdminId(religionId, adminId);
    }

    // ✅ Get active by religion (FIXED)
    @Override
    public List<Caste> getActiveByReligion(Long religionId, Long adminId) {
        return repo.findByReligionIdAndAdminIdAndIsActiveTrue(religionId, adminId);
    }

    // ✅ Search (admin-safe)
    @Override
    public List<Caste> search(String keyword, Long adminId) {
        return repo.findByNameContainingIgnoreCaseAndAdminId(keyword, adminId);
    }
}