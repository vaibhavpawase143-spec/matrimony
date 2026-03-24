package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.model.BrotherType;
import com.example.repository.AdminRepository;
import com.example.repository.BrotherTypeRepository;
import com.example.service.BrotherTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrotherTypeServiceImpl implements BrotherTypeService {

    private final BrotherTypeRepository repo;
    private final AdminRepository adminRepository;

    // ✅ Get all (by admin)
    @Override
    public List<BrotherType> getAll(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active (by admin)
    @Override
    public List<BrotherType> getActive(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Get by ID (secure)
    @Override
    public BrotherType getById(Long id, Long adminId) {
        return repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("BrotherType not found"));
    }

    // ✅ Create
    @Override
    public BrotherType create(BrotherType brotherType, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 🔍 Duplicate check (DB level)
        if (repo.existsByValueIgnoreCaseAndAdminId(
                brotherType.getValue(), adminId)) {
            throw new RuntimeException("BrotherType already exists");
        }

        brotherType.setAdmin(admin);
        brotherType.setIsActive(true);

        return repo.save(brotherType);
    }

    // ✅ Update
    @Override
    public BrotherType update(Long id, BrotherType updated, Long adminId) {

        BrotherType existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("BrotherType not found"));

        // 🔍 Duplicate check (only if value changed)
        if (!existing.getValue().equalsIgnoreCase(updated.getValue()) &&
                repo.existsByValueIgnoreCaseAndAdminId(
                        updated.getValue(), adminId)) {
            throw new RuntimeException("BrotherType already exists");
        }

        existing.setValue(updated.getValue());
        existing.setIsActive(updated.getIsActive());

        return repo.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id, Long adminId) {

        BrotherType existing = repo.findByIdAndAdminId(id, adminId)
                .orElseThrow(() -> new RuntimeException("BrotherType not found"));

        repo.delete(existing);
    }

    // ✅ Search (by admin)
    @Override
    public List<BrotherType> search(String keyword, Long adminId) {
        return repo.findByValueContainingIgnoreCaseAndAdminId(keyword, adminId);
    }
}