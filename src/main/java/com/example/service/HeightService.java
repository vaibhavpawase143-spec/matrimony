package com.example.service;

import com.example.model.Height;

import java.util.List;
import java.util.Optional;

public interface HeightService {

    // 🔍 Basic CRUD
    Height create(Height height);

    Height update(Long id, Height height);

    void delete(Long id);

    Optional<Height> getById(Long id);

    List<Height> getAll();

    // 🔍 Find by height
    Optional<Height> getByHeight(String height);

    Optional<Height> getByHeightIgnoreCase(String height);

    // ✅ Duplicate check
    boolean existsByHeight(String height);

    boolean existsByHeightIgnoreCase(String height);

    // 🔍 Active / Inactive
    List<Height> getActive();

    List<Height> getInactive();

    // 🔍 Admin-based
    List<Height> getByAdmin(Long adminId);

    List<Height> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Height> search(String keyword);

    List<Height> searchByAdmin(Long adminId, String keyword);
}