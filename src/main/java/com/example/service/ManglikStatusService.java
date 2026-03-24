package com.example.service;

import com.example.model.ManglikStatus;

import java.util.List;
import java.util.Optional;

public interface ManglikStatusService {

    // 🔍 Basic CRUD
    ManglikStatus create(ManglikStatus manglikStatus);

    ManglikStatus update(Long id, ManglikStatus manglikStatus);

    void delete(Long id);

    Optional<ManglikStatus> getById(Long id);

    List<ManglikStatus> getAll();

    // 🔍 Find by name
    Optional<ManglikStatus> getByName(String name);

    Optional<ManglikStatus> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<ManglikStatus> getActive();

    List<ManglikStatus> getInactive();

    // 🔍 Admin-based
    List<ManglikStatus> getByAdmin(Long adminId);

    List<ManglikStatus> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<ManglikStatus> search(String keyword);

    List<ManglikStatus> searchByAdmin(Long adminId, String keyword);
}