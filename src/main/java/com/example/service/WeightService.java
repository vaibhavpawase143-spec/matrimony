package com.example.service;

import com.example.dto.request.WeightRequestDTO;
import com.example.dto.response.WeightResponseDTO;

import java.util.List;

public interface WeightService {

    // =====================================================
    // CRUD
    // =====================================================

    WeightResponseDTO create(WeightRequestDTO requestDto);

    WeightResponseDTO update(Long id, WeightRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    WeightResponseDTO getById(Long id);

    List<WeightResponseDTO> getAll();

    List<WeightResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<WeightResponseDTO> getActive();

    List<WeightResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<WeightResponseDTO> getByAdmin(Long adminId);

    List<WeightResponseDTO> getActiveByAdmin(Long adminId);

    List<WeightResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<WeightResponseDTO> search(String keyword);

    List<WeightResponseDTO> searchByAdmin(Long adminId, String keyword);
}