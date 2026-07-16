package com.example.service;

import com.example.dto.request.FamilyRequestDto;
import com.example.dto.response.FamilyResponseDto;

import java.util.List;

public interface FamilyService {

    // =========================
    // CRUD
    // =========================

    FamilyResponseDto create(FamilyRequestDto requestDto);

    FamilyResponseDto update(Long id, FamilyRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FamilyResponseDto getById(Long id);

    List<FamilyResponseDto> getAll();

    List<FamilyResponseDto> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FamilyResponseDto> getActive();

    List<FamilyResponseDto> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyResponseDto> getByAdmin(Long adminId);

    List<FamilyResponseDto> getActiveByAdmin(Long adminId);

    List<FamilyResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyResponseDto> search(String keyword);

    List<FamilyResponseDto> searchByAdmin(Long adminId, String keyword);
}