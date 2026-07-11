package com.example.service;

import com.example.model.MotherTongue;

import java.util.List;
import java.util.Optional;

public interface MotherTongueService {

    // 🔹 CRUD
    MotherTongue create(MotherTongue motherTongue);

    MotherTongue update(Long id, MotherTongue motherTongue);

    void delete(Long id);

    Optional<MotherTongue> getById(Long id);

    // 🔹 Get all (FIXED ✅)
    List<MotherTongue> getAll();

    // 🔹 Admin-based
    List<MotherTongue> getByAdmin(Long adminId);

    List<MotherTongue> getActiveByAdmin(Long adminId);

    List<MotherTongue> getInactiveByAdmin(Long adminId);

    // 🔹 Find by name
    Optional<MotherTongue> getByNameAndAdmin(String name, Long adminId);

    // 🔹 Duplicate check
    boolean existsByNameAndAdmin(String name, Long adminId);

    // 🔹 Search
    List<MotherTongue> searchByAdmin(Long adminId, String keyword);
}