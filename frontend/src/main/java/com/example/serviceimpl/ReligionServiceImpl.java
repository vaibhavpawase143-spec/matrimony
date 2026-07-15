package com.example.serviceimpl;

import com.example.model.Religion;
import com.example.repository.ReligionRepository;
import com.example.service.ReligionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReligionServiceImpl implements ReligionService {

    private final ReligionRepository repository;

    public ReligionServiceImpl(ReligionRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public Religion save(Religion religion) {

        String name = religion.getName();
        Long adminId = religion.getAdmin().getId();

        Optional<Religion> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(religion.getId())) {
            throw new RuntimeException("Religion already exists for this admin!");
        }

        return repository.save(religion);
    }

    // ✅ Get by ID
    @Override
    public Optional<Religion> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Religion> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<Religion> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Religion> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Religion> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<Religion> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<Religion> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}