package com.example.service;

import com.example.model.SubCaste;

import java.util.List;
import java.util.Optional;

public interface SubCasteService {

    // ✅ Create / Save
    SubCaste save(SubCaste subCaste);

    // ✅ Get by ID
    Optional<SubCaste> getById(Long id);

    // 🔍 Get all
    List<SubCaste> getAll();

    // 🔍 Get by admin
    List<SubCaste> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<SubCaste> getActiveByAdmin(Long adminId);

    List<SubCaste> getInactiveByAdmin(Long adminId);

    // 🔍 Filter by caste + admin
    List<SubCaste> getByCasteAndAdmin(Long casteId, Long adminId);

    // 🔍 Active by caste + admin
    List<SubCaste> getActiveByCasteAndAdmin(Long casteId, Long adminId);

    // 🔍 Search
    List<SubCaste> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name
    Optional<SubCaste> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}