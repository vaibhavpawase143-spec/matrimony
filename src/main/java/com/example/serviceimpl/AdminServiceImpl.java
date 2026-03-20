package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository repo;

    // ✅ Get all admins
    @Override
    public List<Admin> getAll() {
        return repo.findAll();
    }

    // ✅ Get admin by ID
    @Override
    public Admin getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    // ✅ Create admin
    @Override
    public Admin create(Admin admin) {

        if (repo.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (repo.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        admin.setisActive(true);

        return repo.save(admin);
    }

    // ✅ Update admin
    @Override
    public Admin update(Long id, Admin updatedAdmin) {

        Admin existing = getById(id);

        existing.setName(updatedAdmin.getName());
        existing.setUsername(updatedAdmin.getUsername());
        existing.setEmail(updatedAdmin.getEmail());
        existing.setPhone(updatedAdmin.getPhone());
        existing.setRole(updatedAdmin.getRole());
        existing.setisActive(updatedAdmin.getisActive());

        return repo.save(existing);
    }

    // ✅ Delete admin
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Login
    @Override
    public Admin login(String username, String password) {

        Optional<Admin> optionalAdmin = repo.findByUsername(username);

        if (optionalAdmin.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        Admin admin = optionalAdmin.get();

        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        admin.setLastLogin(LocalDateTime.now());
        repo.save(admin);

        return admin;
    }
}