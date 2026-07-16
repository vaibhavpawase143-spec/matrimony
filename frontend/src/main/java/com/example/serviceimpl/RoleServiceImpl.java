package com.example.serviceimpl;

import com.example.model.Role;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    // ✅ Save (admin-wise duplicate check)
    @Override
    public Role save(Role role) {

        if (role.getAdmin() == null || role.getAdmin().getId() == null) {
            throw new RuntimeException("Admin must not be null");
        }

        String name = role.getName();
        Long adminId = role.getAdmin().getId();

        Optional<Role> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                (role.getId() == null || !existing.get().getId().equals(role.getId()))) {
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

    // 🔍 Find by name (global)
    @Override
    public Optional<Role> getByName(String name) {
        return repository.findByName(name);
    }
    // 🔍 Get by admin
    @Override
    public List<Role> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active roles by admin
    @Override
    public List<Role> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Inactive roles by admin
    @Override
    public List<Role> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search roles by name
    @Override
    public List<Role> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name + admin (IMPORTANT for JWT)
    @Override
    public Optional<Role> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        repository.deleteById(id);
    }
}