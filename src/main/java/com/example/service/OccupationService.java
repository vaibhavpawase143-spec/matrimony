package com.example.service;

import com.example.dto.request.OccupationRequestDTO;
import com.example.dto.response.OccupationResponseDTO;

import java.util.List;

public interface OccupationService {

    // =========================
    // CRUD
    // =========================

    OccupationResponseDTO create(OccupationRequestDTO requestDto);

    OccupationResponseDTO update(Long id, OccupationRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    OccupationResponseDTO getById(Long id);

    List<OccupationResponseDTO> getAll();

    List<OccupationResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<OccupationResponseDTO> getActive();

    List<OccupationResponseDTO> getInactive();

    // =========================
    // ADMIN
    // =========================

    List<OccupationResponseDTO> getByAdmin(Long adminId);

    List<OccupationResponseDTO> getActiveByAdmin(Long adminId);

    List<OccupationResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<OccupationResponseDTO> search(String keyword);

    List<OccupationResponseDTO> searchByAdmin(Long adminId, String keyword);
}