package com.example.serviceimpl;

import com.example.model.Employed;
import com.example.repository.EmployedRepository;
import com.example.service.EmployedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployedServiceImpl implements EmployedService {

    @Autowired
    private EmployedRepository repo;

    // ✅ Get all
    @Override
    public List<Employed> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<Employed> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public Employed getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employed not found with id: " + id));
    }

    // ✅ Create
    @Override
    public Employed create(Employed emp) {
        emp.setisActive(true);   // only needed field
        return repo.save(emp);
    }

    // ✅ Update
    @Override
    public Employed update(Long id, Employed updated) {
        Employed existing = getById(id);

        existing.setName(updated.getName());
        existing.setisActive(updated.getisActive());

        return repo.save(existing);
    }

    // ✅ Delete (Soft delete)
    @Override
    public void delete(Long id) {
        Employed existing = getById(id);

        existing.setisActive(false);   // deactivate
        repo.save(existing);
    }

    // ✅ Get by admin
    @Override
    public List<Employed> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Employed> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search
    @Override
    public List<Employed> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}