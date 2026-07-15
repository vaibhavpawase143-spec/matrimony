package com.example.service;

import com.example.dto.request.StateRequestDTO;
import com.example.dto.response.StateResponseDTO;

import java.util.List;

public interface StateService {

    // =====================================================
    // CRUD
    // =====================================================

    StateResponseDTO create(StateRequestDTO requestDto);

    StateResponseDTO update(Long id, StateRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    StateResponseDTO getById(Long id);

    List<StateResponseDTO> getAll();

    List<StateResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<StateResponseDTO> getActive();

    List<StateResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<StateResponseDTO> getByAdmin(Long adminId);

    List<StateResponseDTO> getActiveByAdmin(Long adminId);

    List<StateResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // COUNTRY
    // =====================================================

    List<StateResponseDTO> getByCountryAndAdmin(Long countryId, Long adminId);

    List<StateResponseDTO> getActiveByCountryAndAdmin(Long countryId, Long adminId);

    List<StateResponseDTO> getInactiveByCountryAndAdmin(Long countryId, Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<StateResponseDTO> search(String keyword);

    List<StateResponseDTO> searchByAdmin(Long adminId, String keyword);
}