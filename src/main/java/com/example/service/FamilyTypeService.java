package com.example.service;

import com.example.dto.request.FamilyTypeRequestDto;
import com.example.dto.response.FamilyTypeResponseDto;

import java.util.List;

public interface FamilyTypeService {

    // =========================
    // CRUD
    // =========================

    FamilyTypeResponseDto create(FamilyTypeRequestDto requestDto);

    FamilyTypeResponseDto update(Long id, FamilyTypeRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FamilyTypeResponseDto getById(Long id);

    List<FamilyTypeResponseDto> getAll();

    List<FamilyTypeResponseDto> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FamilyTypeResponseDto> getActive();

    List<FamilyTypeResponseDto> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyTypeResponseDto> getByAdmin(Long adminId);

    List<FamilyTypeResponseDto> getActiveByAdmin(Long adminId);

    List<FamilyTypeResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyTypeResponseDto> search(String keyword);

    List<FamilyTypeResponseDto> searchByAdmin(Long adminId, String keyword);
}