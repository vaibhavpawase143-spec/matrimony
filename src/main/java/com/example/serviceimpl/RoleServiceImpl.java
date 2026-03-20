package com.example.serviceimpl;

import com.example.model.Role;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repo;

    // ✅ Create
    @Override
    public Role create(Role role) {
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        role.setIsActive(true); // optional but recommended
        return repo.save(role);
    }

    // ✅ Get all
    @Override
    public List<Role> getAll() {
        return repo.findAll();
    }

    // ✅ Get by ID
    @Override
    public Optional<Role> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public Role update(Long id, Role role) {
        Role existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        existing.setName(role.getName());
        existing.setIsActive(role.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete recommended)
    @Override
    public void delete(Long id) {
        Role existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<Role> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by admin
    @Override
    public List<Role> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }
}