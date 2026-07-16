package com.example.service;

import com.example.dto.request.MaritalStatusRequestDTO;
import com.example.dto.response.MaritalStatusResponseDTO;

import java.util.List;

public interface MaritalStatusService {

    // =========================
    // CRUD
    // =========================

    MaritalStatusResponseDTO create(MaritalStatusRequestDTO requestDto);

    MaritalStatusResponseDTO update(Long id, MaritalStatusRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    MaritalStatusResponseDTO getById(Long id);

    List<MaritalStatusResponseDTO> getAll();

    List<MaritalStatusResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<MaritalStatusResponseDTO> getActive();

    List<MaritalStatusResponseDTO> getInactive();

    // =========================
    // ADMIN
    // =========================

    List<MaritalStatusResponseDTO> getByAdmin(Long adminId);

    List<MaritalStatusResponseDTO> getActiveByAdmin(Long adminId);

    List<MaritalStatusResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<MaritalStatusResponseDTO> search(String keyword);

    List<MaritalStatusResponseDTO> searchByAdmin(Long adminId, String keyword);
}