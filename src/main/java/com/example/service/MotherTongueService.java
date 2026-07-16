package com.example.service;

import com.example.dto.request.MotherTongueRequestDTO;
import com.example.dto.response.MotherTongueResponseDTO;

import java.util.List;

public interface MotherTongueService {

    // =========================
    // CRUD
    // =========================

    MotherTongueResponseDTO create(MotherTongueRequestDTO requestDto);

    MotherTongueResponseDTO update(Long id, MotherTongueRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    MotherTongueResponseDTO getById(Long id);

    List<MotherTongueResponseDTO> getAll();

    List<MotherTongueResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<MotherTongueResponseDTO> getActive();

    List<MotherTongueResponseDTO> getInactive();

    // =========================
    // ADMIN
    // =========================

    List<MotherTongueResponseDTO> getByAdmin(Long adminId);

    List<MotherTongueResponseDTO> getActiveByAdmin(Long adminId);

    List<MotherTongueResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<MotherTongueResponseDTO> search(String keyword);

    List<MotherTongueResponseDTO> searchByAdmin(Long adminId, String keyword);
}