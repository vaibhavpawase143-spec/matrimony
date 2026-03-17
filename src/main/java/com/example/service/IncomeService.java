package com.example.service;

import com.example.model.Income;

import java.util.List;
import java.util.Optional;

public interface IncomeService {

    Income save(Income income);

    Optional<Income> getById(Long id);

    Optional<Income> getByRange(String range);

    List<Income> getAll();

    List<Income> getAllActive();

    List<Income> getAllInactive();

    List<Income> getByAdmin(Long adminId);

    List<Income> getActiveByAdmin(Long adminId);

    List<Income> searchByRange(String keyword);

    Income update(Long id, Income updated);

    void delete(Long id); // soft delete
}