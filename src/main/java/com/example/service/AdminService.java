package com.example.service;

import com.example.model.Admin;
import com.example.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repo;

    // ✅ Get all admins
    public List<Admin> getAll() {
        return repo.findAll();
    }

    // ✅ Get admin by ID
    public Admin getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    // ✅ Create admin (with validation)
    public Admin create(Admin admin) {

        // Check duplicate username
        if (repo.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check duplicate email
        if (repo.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Set default values
        admin.setStatus(true);

        return repo.save(admin);
    }

    // ✅ Update admin
    public Admin update(Long id, Admin updatedAdmin) {

        Admin existing = getById(id);

        existing.setName(updatedAdmin.getName());
        existing.setUsername(updatedAdmin.getUsername());
        existing.setEmail(updatedAdmin.getEmail());
        existing.setPhone(updatedAdmin.getPhone());
        existing.setRole(updatedAdmin.getRole());
        existing.setStatus(updatedAdmin.getStatus());

        return repo.save(existing);
    }

    // ✅ Delete admin
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Login (basic)
    public Admin login(String username, String password) {

        Optional<Admin> optionalAdmin = repo.findByUsername(username);

        if (optionalAdmin.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        Admin admin = optionalAdmin.get();

        // NOTE: plain text check (later use BCrypt)
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        // Update last login
        admin.setLastLogin(LocalDateTime.now());
        repo.save(admin);

        return admin;
    }
}