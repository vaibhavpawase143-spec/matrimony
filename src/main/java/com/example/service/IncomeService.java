package com.example.service;

import com.example.dto.request.IncomeRequestDTO;
import com.example.dto.response.IncomeResponseDTO;

import java.util.List;

public interface IncomeService {

    // =========================
    // CRUD
    // =========================

    IncomeResponseDTO create(IncomeRequestDTO requestDto);

    IncomeResponseDTO update(Long id, IncomeRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    IncomeResponseDTO getById(Long id);

    List<IncomeResponseDTO> getAll();

    List<IncomeResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<IncomeResponseDTO> getActive();

    List<IncomeResponseDTO> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<IncomeResponseDTO> getByAdmin(Long adminId);

    List<IncomeResponseDTO> getActiveByAdmin(Long adminId);

    List<IncomeResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<IncomeResponseDTO> search(String keyword);

    List<IncomeResponseDTO> searchByAdmin(Long adminId, String keyword);
}