package com.example.serviceimpl;

import com.example.model.SisterType;
import com.example.repository.SisterTypeRepository;
import com.example.service.SisterTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SisterTypeServiceImpl implements SisterTypeService {

    private final SisterTypeRepository repository;

    public SisterTypeServiceImpl(SisterTypeRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check using value)
    @Override
    public SisterType save(SisterType sisterType) {

        String value = sisterType.getValue();
        Long adminId = sisterType.getAdmin().getId();

        Optional<SisterType> existing =
                repository.findByValueIgnoreCaseAndAdminId(value, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(sisterType.getId())) {
            throw new RuntimeException("SisterType already exists for this admin!");
        }

        return repository.save(sisterType);
    }

    // ✅ Get by ID
    @Override
    public Optional<SisterType> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<SisterType> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<SisterType> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<SisterType> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<SisterType> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<SisterType> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by value
    @Override
    public Optional<SisterType> getByValueAndAdmin(String value, Long adminId) {
        return repository.findByValueIgnoreCaseAndAdminId(value, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}