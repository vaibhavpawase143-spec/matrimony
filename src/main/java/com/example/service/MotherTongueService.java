package com.example.service;

import com.example.model.MotherTongue;

import java.util.List;
import java.util.Optional;

public interface MotherTongueService {

    // 🔍 Basic CRUD (admin-based)
    MotherTongue create(MotherTongue motherTongue);

    MotherTongue update(Long id, MotherTongue motherTongue);

    void delete(Long id);

    Optional<MotherTongue> getById(Long id);

    // 🔍 Admin-based
    List<MotherTongue> getByAdmin(Long adminId);

    List<MotherTongue> getActiveByAdmin(Long adminId);

    List<MotherTongue> getInactiveByAdmin(Long adminId);

    // 🔍 Find by name (admin-specific)
    Optional<MotherTongue> getByNameAndAdmin(String name, Long adminId);

    // 🔍 Duplicate check (admin-specific)
    boolean existsByNameAndAdmin(String name, Long adminId);

    // 🔍 Search
    List<MotherTongue> searchByAdmin(Long adminId, String keyword);
}