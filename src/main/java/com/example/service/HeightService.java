package com.example.service;

import com.example.dto.request.HeightRequestDTO;
import com.example.dto.response.HeightResponseDTO;

import java.util.List;

public interface HeightService {

    // =========================
    // CRUD
    // =========================

    HeightResponseDTO create(HeightRequestDTO requestDto);

    HeightResponseDTO update(Long id, HeightRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    HeightResponseDTO getById(Long id);

    List<HeightResponseDTO> getAll();

    List<HeightResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<HeightResponseDTO> getActive();

    List<HeightResponseDTO> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<HeightResponseDTO> getByAdmin(Long adminId);

    List<HeightResponseDTO> getActiveByAdmin(Long adminId);

    List<HeightResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<HeightResponseDTO> search(String keyword);

    List<HeightResponseDTO> searchByAdmin(Long adminId, String keyword);
}