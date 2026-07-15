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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional   // 🔥 IMPORTANT FIX
public class CasteServiceImpl implements CasteService {

    private final CasteRepository repo;
    private final AdminRepository adminRepository;
    private final ReligionRepository religionRepository;

    // ================= GET ALL =================
    @Override
    public List<Caste> getAll(Long adminId) {

        List<Caste> list = repo.findByAdminId(adminId);

        // 🔥 FIX: initialize lazy relation
        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }

    // ================= GET ACTIVE =================
    @Override
    public List<Caste> getActive(Long adminId) {

        List<Caste> list = repo.findByAdminIdAndIsActiveTrue(adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }

    // ================= GET BY ID =================
    @Override
    public Caste getById(Long id, Long adminId) {

        Caste caste = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        // 🔥 FIX
        if (caste.getReligion() != null) {
            caste.getReligion().getName();
        }

        return caste;
    }

    // ================= CREATE =================
    @Override
    public Caste create(Caste caste, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Religion religion = religionRepository.findById(caste.getReligion().getId())
                .orElseThrow(() -> new RuntimeException("Religion not found"));

        if (repo.existsByNameIgnoreCaseAndReligionIdAndAdminId(
                caste.getName(), religion.getId(), adminId)) {
            throw new RuntimeException("Caste already exists");
        }

        caste.setAdmin(admin);
        caste.setReligion(religion);
        caste.setIsActive(true);

        return repo.save(caste);
    }

    // ================= UPDATE =================
    @Override
    public Caste update(Long id, Caste updated, Long adminId) {

        Caste existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        Religion religion = religionRepository.findById(updated.getReligion().getId())
                .orElseThrow(() -> new RuntimeException("Religion not found"));

        if (!existing.getName().equalsIgnoreCase(updated.getName()) &&
                repo.existsByNameIgnoreCaseAndReligionIdAndAdminId(
                        updated.getName(), religion.getId(), adminId)) {
            throw new RuntimeException("Caste already exists");
        }

        existing.setName(updated.getName());
        existing.setReligion(religion);
        existing.setIsActive(updated.getIsActive());

        return repo.save(existing);
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id, Long adminId) {

        Caste existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        repo.delete(existing);
    }

    // ================= GET BY RELIGION =================
    @Override
    public List<Caste> getByReligion(Long religionId, Long adminId) {

        List<Caste> list = repo.findByReligionIdAndAdminId(religionId, adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }

    // ================= GET ACTIVE BY RELIGION =================
    @Override
    public List<Caste> getActiveByReligion(Long religionId, Long adminId) {

        List<Caste> list = repo.findByReligionIdAndAdminIdAndIsActiveTrue(religionId, adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }

    // ================= SEARCH =================
    @Override
    public List<Caste> search(String keyword, Long adminId) {

        List<Caste> list = repo.findByNameContainingIgnoreCaseAndAdminId(keyword, adminId);

        list.forEach(c -> {
            if (c.getReligion() != null) {
                c.getReligion().getName();
            }
        });

        return list;
    }
}