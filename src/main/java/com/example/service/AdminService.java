package com.example.service;

import com.example.model.Admin;
import com.example.model.UserPhoto;

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

    interface UserPhotoService {

        UserPhoto uploadPhoto(Long userId, String photoType, String photoUrl);

        List<UserPhoto> getUserPhotos(Long userId);

        List<UserPhoto> getByType(Long userId, String photoType);

        void deleteByType(Long userId, String photoType);
    }
}