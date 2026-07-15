package com.example.service;

import com.example.dto.request.SisterTypeRequestDTO;
import com.example.dto.response.SisterTypeResponseDTO;

import java.util.List;

public interface SisterTypeService {

    // =====================================================
    // CRUD
    // =====================================================

    SisterTypeResponseDTO create(SisterTypeRequestDTO requestDto);

    SisterTypeResponseDTO update(Long id, SisterTypeRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    SisterTypeResponseDTO getById(Long id);

    List<SisterTypeResponseDTO> getAll();

    List<SisterTypeResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SisterTypeResponseDTO> getActive();

    List<SisterTypeResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SisterTypeResponseDTO> getByAdmin(Long adminId);

    List<SisterTypeResponseDTO> getActiveByAdmin(Long adminId);

    List<SisterTypeResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SisterTypeResponseDTO> search(String keyword);

    List<SisterTypeResponseDTO> searchByAdmin(Long adminId, String keyword);
}