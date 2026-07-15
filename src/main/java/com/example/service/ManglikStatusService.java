package com.example.service;

import com.example.dto.request.ManglikStatusRequestDTO;
import com.example.dto.response.ManglikStatusResponseDTO;

import java.util.List;

public interface ManglikStatusService {

    // =========================
    // CRUD
    // =========================

    ManglikStatusResponseDTO create(ManglikStatusRequestDTO requestDto);

    ManglikStatusResponseDTO update(Long id, ManglikStatusRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    ManglikStatusResponseDTO getById(Long id);

    List<ManglikStatusResponseDTO> getAll();

    List<ManglikStatusResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<ManglikStatusResponseDTO> getActive();

    List<ManglikStatusResponseDTO> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<ManglikStatusResponseDTO> getByAdmin(Long adminId);

    List<ManglikStatusResponseDTO> getActiveByAdmin(Long adminId);

    List<ManglikStatusResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<ManglikStatusResponseDTO> search(String keyword);

    List<ManglikStatusResponseDTO> searchByAdmin(Long adminId, String keyword);
}