package com.example.service;

import com.example.dto.request.SmokingRequestDTO;
import com.example.dto.response.SmokingResponseDTO;

import java.util.List;

public interface SmokingService {

    // =====================================================
    // CREATE
    // =====================================================

    SmokingResponseDTO create(SmokingRequestDTO requestDto);

    // =====================================================
    // UPDATE
    // =====================================================

    SmokingResponseDTO update(Long id, SmokingRequestDTO requestDto);

    // =====================================================
    // DELETE
    // =====================================================

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    SmokingResponseDTO getById(Long id);

    List<SmokingResponseDTO> getAll();

    List<SmokingResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SmokingResponseDTO> getActive();

    List<SmokingResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SmokingResponseDTO> getByAdmin(Long adminId);

    List<SmokingResponseDTO> getActiveByAdmin(Long adminId);

    List<SmokingResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SmokingResponseDTO> search(String keyword);

    List<SmokingResponseDTO> searchByAdmin(Long adminId, String keyword);
}