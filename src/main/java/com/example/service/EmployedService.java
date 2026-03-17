package com.example.service;

import com.example.model.Employed;
import com.example.repository.EmployedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployedService {

    @Autowired
    private EmployedRepository repo;

    // ✅ Get all
    public List<Employed> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    public List<Employed> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Employed getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employed type not found"));
    }

    // ✅ Create
    public Employed create(Employed emp) {

        if (repo.existsByName(emp.getName())) {
            throw new RuntimeException("Employed type already exists");
        }

        emp.setStatus(true);

        return repo.save(emp);
    }

    // ✅ Update
    public Employed update(Long id, Employed updated) {

        Employed existing = getById(id);

        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("Employed type already exists");
        }

        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());

        return repo.save(existing);
    }

    // ✅ Delete
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<Employed> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<Employed> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search
    public List<Employed> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}