package com.example.service;

import com.example.model.Admin;

import java.util.List;

public interface AdminService {

    // ================= REGISTER =================
    Admin register(Admin admin);

    Admin create(Admin admin);

    Admin getById(Long id);

    List<Admin> getAll();

    Admin update(Long id, Admin admin);

    void delete(Long id);

    Admin login(String username, String password);
}