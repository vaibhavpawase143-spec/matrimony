package com.example.serviceimpl;

import com.example.model.MotherTongue;
import com.example.repository.MotherTongueRepository;
import com.example.service.MotherTongueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotherTongueServiceImpl implements MotherTongueService {

    private final MotherTongueRepository motherTongueRepository;

    public MotherTongueServiceImpl(MotherTongueRepository motherTongueRepository) {
        this.motherTongueRepository = motherTongueRepository;
    }

    // ✅ Create
    @Override
    public MotherTongue create(MotherTongue motherTongue) {

        Long adminId = motherTongue.getAdmin().getId();

        if (motherTongueRepository.existsByNameIgnoreCaseAndAdminId(
                motherTongue.getName(), adminId)) {
            throw new RuntimeException(
                    "MotherTongue already exists for this admin: " + motherTongue.getName()
            );
        }

        return motherTongueRepository.save(motherTongue);
    }

    // 🔄 Update
    @Override
    public MotherTongue update(Long id, MotherTongue motherTongue) {

        MotherTongue existing = motherTongueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MotherTongue not found with id: " + id));

        Long adminId = existing.getAdmin().getId();

        motherTongueRepository.findByNameIgnoreCaseAndAdminId(
                motherTongue.getName(), adminId
        ).ifPresent(m -> {
            if (!m.getId().equals(id)) {
                throw new RuntimeException(
                        "MotherTongue already exists for this admin: " + motherTongue.getName()
                );
            }
        });

        // ✏️ Update fields
        existing.setName(motherTongue.getName());
        existing.setIsActive(motherTongue.getIsActive());

        return motherTongueRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {

        MotherTongue existing = motherTongueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MotherTongue not found with id: " + id));

        motherTongueRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<MotherTongue> getById(Long id) {
        return motherTongueRepository.findById(id);
    }

    // 🔍 Get all ✅ FIXED
    @Override
    public List<MotherTongue> getAll() {
        return motherTongueRepository.findAll();
    }

    // 🔍 Admin-based
    @Override
    public List<MotherTongue> getByAdmin(Long adminId) {
        return motherTongueRepository.findByAdminId(adminId);
    }

    @Override
    public List<MotherTongue> getActiveByAdmin(Long adminId) {
        return motherTongueRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<MotherTongue> getInactiveByAdmin(Long adminId) {
        return motherTongueRepository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Find by name
    @Override
    public Optional<MotherTongue> getByNameAndAdmin(String name, Long adminId) {
        return motherTongueRepository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByNameAndAdmin(String name, Long adminId) {
        return motherTongueRepository.existsByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // 🔍 Search
    @Override
    public List<MotherTongue> searchByAdmin(Long adminId, String keyword) {
        return motherTongueRepository
                .findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}