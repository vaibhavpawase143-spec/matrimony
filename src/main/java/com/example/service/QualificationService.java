package com.example.service;

import com.example.dto.request.QualificationRequestDTO;
import com.example.dto.response.QualificationResponseDTO;

import java.util.List;

public interface QualificationService {

    // =========================
    // CRUD
    // =========================

    QualificationResponseDTO create(QualificationRequestDTO requestDto);

    QualificationResponseDTO update(Long id, QualificationRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    QualificationResponseDTO getById(Long id);

    List<QualificationResponseDTO> getAll();

    List<QualificationResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<QualificationResponseDTO> getActive();

    List<QualificationResponseDTO> getInactive();

    // =========================
    // ADMIN
    // =========================

    List<QualificationResponseDTO> getByAdmin(Long adminId);

    List<QualificationResponseDTO> getActiveByAdmin(Long adminId);

    List<QualificationResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<QualificationResponseDTO> search(String keyword);

    List<QualificationResponseDTO> searchByAdmin(Long adminId, String keyword);
}