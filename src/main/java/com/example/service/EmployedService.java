package com.example.service;

import com.example.dto.request.EmployedRequestDto;
import com.example.dto.response.EmployedResponseDto;

import java.util.List;

public interface EmployedService {

    // =========================
    // CRUD
    // =========================

    EmployedResponseDto create(EmployedRequestDto requestDto);

    EmployedResponseDto update(Long id, EmployedRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // Get
    // =========================

    EmployedResponseDto getById(Long id);

    List<EmployedResponseDto> getAll();

    List<EmployedResponseDto> getDeleted();

    // =========================
    // Active / Inactive
    // =========================

    List<EmployedResponseDto> getActive();

    List<EmployedResponseDto> getInactive();

    // =========================
    // Admin Wise
    // =========================

    List<EmployedResponseDto> getByAdmin(Long adminId);

    List<EmployedResponseDto> getActiveByAdmin(Long adminId);

    List<EmployedResponseDto> getInactiveByAdmin(Long adminId);

    // =========================
    // Search
    // =========================

    List<EmployedResponseDto> search(String keyword);

    List<EmployedResponseDto> searchByAdmin(Long adminId, String keyword);
}