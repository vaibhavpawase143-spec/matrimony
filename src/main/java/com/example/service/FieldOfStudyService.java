package com.example.service;

import com.example.dto.request.FieldOfStudyRequestDTO;
import com.example.dto.response.FieldOfStudyResponseDTO;

import java.util.List;

public interface FieldOfStudyService {

    // =========================
    // CRUD
    // =========================

    FieldOfStudyResponseDTO create(FieldOfStudyRequestDTO requestDto);

    FieldOfStudyResponseDTO update(Long id, FieldOfStudyRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FieldOfStudyResponseDTO getById(Long id);

    List<FieldOfStudyResponseDTO> getAll();

    List<FieldOfStudyResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FieldOfStudyResponseDTO> getActive();

    List<FieldOfStudyResponseDTO> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<FieldOfStudyResponseDTO> getByAdmin(Long adminId);

    List<FieldOfStudyResponseDTO> getActiveByAdmin(Long adminId);

    List<FieldOfStudyResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FieldOfStudyResponseDTO> search(String keyword);

    List<FieldOfStudyResponseDTO> searchByAdmin(Long adminId, String keyword);
}