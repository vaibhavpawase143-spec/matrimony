package com.example.service;

import com.example.model.Income;

import java.util.List;
import java.util.Optional;

public interface IncomeService {

    // 🔍 Basic CRUD
    Income create(Income income);

    Income update(Long id, Income income);

    void delete(Long id);

    Optional<Income> getById(Long id);

    List<Income> getAll();

    // 🔍 Find by range
    Optional<Income> getByRange(String range);

    Optional<Income> getByRangeIgnoreCase(String range);

    // ✅ Duplicate check
    boolean existsByRange(String range);

    boolean existsByRangeIgnoreCase(String range);

    // 🔍 Active / Inactive
    List<Income> getActive();

    List<Income> getInactive();

    // 🔍 Admin-based
    List<Income> getByAdmin(Long adminId);

    List<Income> getActiveByAdmin(Long adminId);

    // 🔍 Search
    List<Income> search(String keyword);

    List<Income> searchByAdmin(Long adminId, String keyword);
}