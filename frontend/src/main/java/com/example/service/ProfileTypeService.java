package com.example.service;

import com.example.model.ProfileType;

import java.util.List;
import java.util.Optional;

public interface ProfileTypeService {

    // ✅ Create / Save
    ProfileType save(ProfileType profileType);

    // ✅ Get by ID
    Optional<ProfileType> getById(Long id);

    // 🔍 Get all
    List<ProfileType> getAll();

    // 🔍 Get by admin
    List<ProfileType> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<ProfileType> getActiveByAdmin(Long adminId);

    List<ProfileType> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<ProfileType> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name (admin-specific)
    Optional<ProfileType> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}