package com.example.service;

import com.example.dto.request.SubCasteRequestDTO;
import com.example.dto.response.SubCasteResponseDTO;

import java.util.List;

public interface SubCasteService {

    // =====================================================
    // CRUD
    // =====================================================

    SubCasteResponseDTO create(SubCasteRequestDTO requestDto);

    SubCasteResponseDTO update(Long id, SubCasteRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    SubCasteResponseDTO getById(Long id);

    List<SubCasteResponseDTO> getAll();

    List<SubCasteResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SubCasteResponseDTO> getActive();

    List<SubCasteResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SubCasteResponseDTO> getByAdmin(Long adminId);

    List<SubCasteResponseDTO> getActiveByAdmin(Long adminId);

    List<SubCasteResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // CASTE
    // =====================================================

    List<SubCasteResponseDTO> getByCasteAndAdmin(Long casteId, Long adminId);

    List<SubCasteResponseDTO> getActiveByCasteAndAdmin(Long casteId, Long adminId);

    List<SubCasteResponseDTO> getInactiveByCasteAndAdmin(Long casteId, Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SubCasteResponseDTO> search(String keyword);

    List<SubCasteResponseDTO> searchByAdmin(Long adminId, String keyword);
}