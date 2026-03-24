package com.example.serviceimpl;

import com.example.model.Employed;
import com.example.repository.EmployedRepository;
import com.example.service.EmployedService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployedServiceImpl implements EmployedService {

    private final EmployedRepository employedRepository;

    public EmployedServiceImpl(EmployedRepository employedRepository) {
        this.employedRepository = employedRepository;
    }

    // ✅ Create
    @Override
    public Employed create(Employed employed) {

        if (employedRepository.existsByNameIgnoreCase(employed.getName())) {
            throw new RuntimeException("Employed already exists: " + employed.getName());
        }

        return employedRepository.save(employed);
    }

    // 🔄 Update
    @Override
    public Employed update(Long id, Employed employed) {

        Employed existing = employedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employed not found with id: " + id));

        employedRepository.findByNameIgnoreCase(employed.getName())
                .ifPresent(e -> {
                    if (!e.getId().equals(id)) {
                        throw new RuntimeException("Employed already exists: " + employed.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(employed.getName());
        existing.setIsActive(employed.getIsActive());

        return employedRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Employed existing = employedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employed not found with id: " + id));

        employedRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Employed> getById(Long id) {
        return employedRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Employed> getAll() {
        return employedRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<Employed> getByName(String name) {
        return employedRepository.findByName(name);
    }

    @Override
    public Optional<Employed> getByNameIgnoreCase(String name) {
        return employedRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return employedRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return employedRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Employed> getActive() {
        return employedRepository.findByIsActiveTrue();
    }

    @Override
    public List<Employed> getInactive() {
        return employedRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Employed> getByAdmin(Long adminId) {
        return employedRepository.findByAdminId(adminId);
    }

    @Override
    public List<Employed> getActiveByAdmin(Long adminId) {
        return employedRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Employed> search(String keyword) {
        return employedRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Employed> searchByAdmin(Long adminId, String keyword) {
        return employedRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}