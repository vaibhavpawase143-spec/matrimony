package com.example.service;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;

import java.util.List;

public interface ReligionService {

    // =====================================================
    // CRUD
    // =====================================================

    ReligionResponseDTO create(ReligionRequestDTO requestDto);

    ReligionResponseDTO update(Long id, ReligionRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    ReligionResponseDTO getById(Long id);

    List<ReligionResponseDTO> getAll();

    List<ReligionResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<ReligionResponseDTO> getActive();

    List<ReligionResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<ReligionResponseDTO> getByAdmin(Long adminId);

    List<ReligionResponseDTO> getActiveByAdmin(Long adminId);

    List<ReligionResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<ReligionResponseDTO> search(String keyword);

    List<ReligionResponseDTO> searchByAdmin(Long adminId, String keyword);
}