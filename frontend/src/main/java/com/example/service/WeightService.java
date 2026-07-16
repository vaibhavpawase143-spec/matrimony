package com.example.service;

import com.example.model.Weight;

import java.util.List;
import java.util.Optional;

public interface WeightService {

    // ✅ Create / Save
    Weight save(Weight weight);

    // 🔍 Get by ID
    Optional<Weight> getById(Long id);

    // 🔍 Get all
    List<Weight> getAll();

    // 🔍 Get by admin
    List<Weight> getByAdmin(Long adminId);

    // 🔍 Active / Inactive
    List<Weight> getActiveByAdmin(Long adminId);

    List<Weight> getInactiveByAdmin(Long adminId);

    // 🔍 Search
    List<Weight> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by value
    Optional<Weight> getByValueAndAdmin(String value, Long adminId);

    // ❌ Delete (soft delete recommended)
    void delete(Long id);
}