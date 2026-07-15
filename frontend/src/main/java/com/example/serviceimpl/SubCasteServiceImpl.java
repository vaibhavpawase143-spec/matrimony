package com.example.serviceimpl;

import com.example.model.SubCaste;
import com.example.repository.SubCasteRepository;
import com.example.service.SubCasteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCasteServiceImpl implements SubCasteService {

    private final SubCasteRepository repository;

    public SubCasteServiceImpl(SubCasteRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public SubCaste save(SubCaste subCaste) {

        String name = subCaste.getName();
        Long adminId = subCaste.getAdmin().getId();

        Optional<SubCaste> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(subCaste.getId())) {
            throw new RuntimeException("SubCaste already exists for this admin!");
        }

        return repository.save(subCaste);
    }

    // ✅ Get by ID
    @Override
    public Optional<SubCaste> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<SubCaste> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<SubCaste> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<SubCaste> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<SubCaste> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Caste + admin
    @Override
    public List<SubCaste> getByCasteAndAdmin(Long casteId, Long adminId) {
        return repository.findByCaste_IdAndAdminId(casteId, adminId);
    }

    // 🔍 Active by caste + admin
    @Override
    public List<SubCaste> getActiveByCasteAndAdmin(Long casteId, Long adminId) {
        return repository.findByCaste_IdAndAdminIdAndIsActiveTrue(casteId, adminId);
    }

    // 🔍 Search
    @Override
    public List<SubCaste> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<SubCaste> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}