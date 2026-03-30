package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.model.Role;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @Override
    public Admin register(Admin admin) {

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 🔐 Encode password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // 👑 Assign ADMIN role (FIXED)
        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        admin.setRole(role);
        admin.setIsActive(true);

        return adminRepository.save(admin);
    }

    // ================= CREATE =================
    @Override
    public Admin create(Admin admin) {

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 🔐 Encode password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // 👑 Ensure ADMIN role here also (important)
        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        admin.setRole(role);
        admin.setIsActive(true);

        return adminRepository.save(admin);
    }

    // ================= GET =================
    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    // ================= UPDATE =================
    @Override
    public Admin update(Long id, Admin updatedAdmin) {

        Admin existing = getById(id);

        existing.setName(updatedAdmin.getName());
        existing.setEmail(updatedAdmin.getEmail());
        existing.setUsername(updatedAdmin.getUsername());

        // 🔐 Encode password if changed
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        return adminRepository.save(existing);
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        Admin admin = getById(id);
        adminRepository.delete(admin);
    }

    // ================= LOGIN =================
    @Override
    public Admin login(String username, String password) {

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        // 🔐 Password check
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return admin;
    }
}