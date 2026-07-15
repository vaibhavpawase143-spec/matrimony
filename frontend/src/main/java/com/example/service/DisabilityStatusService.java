package com.example.service;

import com.example.model.DisabilityStatus;

import java.util.List;
import java.util.Optional;

public interface DisabilityStatusService {

    // 🔍 Basic CRUD
    DisabilityStatus create(DisabilityStatus disabilityStatus);

    DisabilityStatus update(Long id, DisabilityStatus disabilityStatus);

    void delete(Long id);

    Optional<DisabilityStatus> getById(Long id);

    List<DisabilityStatus> getAll();

    // 🔍 Find by value
    Optional<DisabilityStatus> getByValue(String value);

    Optional<DisabilityStatus> getByValueIgnoreCase(String value);

    // ✅ Duplicate check
    boolean existsByValue(String value);

    boolean existsByValueIgnoreCase(String value);

    // 🔍 Active / Inactive
    List<DisabilityStatus> getActive();

    List<DisabilityStatus> getInactive();

    // 🔍 Admin-based
    List<DisabilityStatus> getByAdmin(Long adminId);

    List<DisabilityStatus> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<DisabilityStatus> search(String keyword);

    List<DisabilityStatus> searchByAdmin(Long adminId, String keyword);
}