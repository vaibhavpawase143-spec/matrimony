package com.example.service;

import com.example.dto.request.FamilyStatusRequestDto;
import com.example.dto.response.FamilyStatusResponseDto;

import java.util.List;

public interface FamilyStatusService {

    // =========================
    // CRUD
    // =========================

    FamilyStatusResponseDto create(FamilyStatusRequestDto requestDto);

    FamilyStatusResponseDto update(Long id, FamilyStatusRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FamilyStatusResponseDto getById(Long id);

    List<FamilyStatusResponseDto> getAll();

    List<FamilyStatusResponseDto> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<FamilyStatusResponseDto> getActive();

    List<FamilyStatusResponseDto> getInactive();

    // =========================
    // ADMIN WISE
    // =========================

    List<FamilyStatusResponseDto> getByAdmin(Long adminId);

    List<FamilyStatusResponseDto> getActiveByAdmin(Long adminId);

    List<FamilyStatusResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<FamilyStatusResponseDto> search(String keyword);

    List<FamilyStatusResponseDto> searchByAdmin(Long adminId, String keyword);
}