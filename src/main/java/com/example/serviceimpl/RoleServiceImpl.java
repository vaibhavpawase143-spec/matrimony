package com.example.serviceimpl;

import com.example.model.Role;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public Role save(Role role) {

        String name = role.getName();
        Long adminId = role.getAdmin().getId();

        Optional<Role> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(role.getId())) {
            throw new RuntimeException("Role already exists for this admin!");
        }

        return repository.save(role);
    }

    // ✅ Get by ID
    @Override
    public Optional<Role> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<Role> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Role> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Role> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<Role> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<Role> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}