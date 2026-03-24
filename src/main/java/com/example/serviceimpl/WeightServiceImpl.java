package com.example.serviceimpl;

import com.example.model.Weight;
import com.example.repository.WeightRepository;
import com.example.service.WeightService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeightServiceImpl implements WeightService {

    private final WeightRepository repository;

    public WeightServiceImpl(WeightRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public Weight save(Weight weight) {

        String value = weight.getValue();
        Long adminId = weight.getAdmin().getId();

        Optional<Weight> existing =
                repository.findByValueIgnoreCaseAndAdminId(value, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(weight.getId())) {
            throw new RuntimeException("Weight already exists for this admin!");
        }

        return repository.save(weight);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Weight> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Weight> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<Weight> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active
    @Override
    public List<Weight> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Inactive
    @Override
    public List<Weight> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<Weight> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndValueContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by value
    @Override
    public Optional<Weight> getByValueAndAdmin(String value, Long adminId) {
        return repository.findByValueIgnoreCaseAndAdminId(value, adminId);
    }

    // ❌ Soft delete
    @Override
    public void delete(Long id) {

        Weight weight = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weight not found!"));

        weight.setIsActive(false);
        repository.save(weight);
    }
}