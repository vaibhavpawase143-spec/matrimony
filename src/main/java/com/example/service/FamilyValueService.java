package com.example.service;

import com.example.dto.request.FamilyValueRequestDto;
import com.example.dto.response.FamilyValueResponseDto;

import java.util.List;

public interface FamilyValueService {

    // =========================
    // CRUD
    // =========================

    FamilyValueResponseDto create(FamilyValueRequestDto requestDto);

    FamilyValueResponseDto update(Long id, FamilyValueRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FamilyValueResponseDto getById(Long id);

    List<FamilyValueResponseDto> getAll();

    List<FamilyValueResponseDto> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FamilyValueResponseDto> getActive();

    List<FamilyValueResponseDto> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyValueResponseDto> getByAdmin(Long adminId);

    List<FamilyValueResponseDto> getActiveByAdmin(Long adminId);

    List<FamilyValueResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyValueResponseDto> search(String keyword);

    List<FamilyValueResponseDto> searchByAdmin(Long adminId, String keyword);
}