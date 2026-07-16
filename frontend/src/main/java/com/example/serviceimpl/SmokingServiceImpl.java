package com.example.serviceimpl;

import com.example.model.Smoking;
import com.example.repository.SmokingRepository;
import com.example.service.SmokingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmokingServiceImpl implements SmokingService {

    private final SmokingRepository repository;

    public SmokingServiceImpl(SmokingRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check using value)
    @Override
    public Smoking save(Smoking smoking) {

        String value = smoking.getValue();
        Long adminId = smoking.getAdmin().getId();

        Optional<Smoking> existing =
                repository.findByValueIgnoreCaseAndAdminId(value, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(smoking.getId())) {
            throw new RuntimeException("Smoking type already exists for this admin!");
        }

        return repository.save(smoking);
    }

    // ✅ Get by ID
    @Override
    public Optional<Smoking> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Smoking> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<Smoking> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Smoking> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Smoking> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<Smoking> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by value
    @Override
    public Optional<Smoking> getByValueAndAdmin(String value, Long adminId) {
        return repository.findByValueIgnoreCaseAndAdminId(value, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}