package com.example.service;

import com.example.model.Admin;
import java.util.List;

public interface AdminService {

    // ✅ Get all admins
    List<Admin> getAll();

    // ✅ Get admin by ID
    Admin getById(Long id);

    // ✅ Create admin
    Admin create(Admin admin);

    // ✅ Update admin
    Admin update(Long id, Admin admin);

    // ✅ Delete admin
    void delete(Long id);

    // ✅ Login
    Admin login(String username, String password);
}