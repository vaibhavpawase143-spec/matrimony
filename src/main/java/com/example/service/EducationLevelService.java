package com.example.service;

import com.example.dto.request.EducationLevelRequestDto;
import com.example.dto.response.EducationLevelResponseDto;

import java.util.List;

public interface EducationLevelService {

    // =========================
    // CRUD
    // =========================

    EducationLevelResponseDto create(EducationLevelRequestDto requestDto);

    EducationLevelResponseDto update(Long id, EducationLevelRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // Get
    // =========================

    EducationLevelResponseDto getById(Long id);

    List<EducationLevelResponseDto> getAll();

    List<EducationLevelResponseDto> getDeleted();

    // =========================
    // Active / Inactive
    // =========================

    List<EducationLevelResponseDto> getActive();

    List<EducationLevelResponseDto> getInactive();

    // =========================
    // Admin Wise
    // =========================

    List<EducationLevelResponseDto> getByAdmin(Long adminId);

    List<EducationLevelResponseDto> getActiveByAdmin(Long adminId);

    List<EducationLevelResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // Search
    // =========================

    List<EducationLevelResponseDto> search(String keyword);

    List<EducationLevelResponseDto> searchByAdmin(Long adminId, String keyword);
}