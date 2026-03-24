package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin create(Admin admin) {

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin update(Long id, Admin updatedAdmin) {

        Admin existing = getById(id);

        existing.setName(updatedAdmin.getName());
        existing.setEmail(updatedAdmin.getEmail());
        existing.setUsername(updatedAdmin.getUsername());
        existing.setPassword(updatedAdmin.getPassword());

        return adminRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Admin admin = getById(id);
        adminRepository.delete(admin);
    }

    @Override
    public Admin login(String username, String password) {

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return admin;
    }
}