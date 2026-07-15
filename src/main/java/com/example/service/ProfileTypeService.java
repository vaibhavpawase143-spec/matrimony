package com.example.service;

import com.example.dto.request.ProfileTypeRequestDTO;
import com.example.dto.response.ProfileTypeResponseDTO;

import java.util.List;

public interface ProfileTypeService {

    // =========================
    // CRUD
    // =========================

    ProfileTypeResponseDTO create(ProfileTypeRequestDTO requestDto);

    ProfileTypeResponseDTO update(Long id, ProfileTypeRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    ProfileTypeResponseDTO getById(Long id);

    List<ProfileTypeResponseDTO> getAll();

    List<ProfileTypeResponseDTO> getDeleted();

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    List<ProfileTypeResponseDTO> getActive();

    List<ProfileTypeResponseDTO> getInactive();

    // =========================
    // ADMIN
    // =========================

    List<ProfileTypeResponseDTO> getByAdmin(Long adminId);

    List<ProfileTypeResponseDTO> getActiveByAdmin(Long adminId);

    List<ProfileTypeResponseDTO> getInactiveByAdmin(Long adminId);

    // =========================
    // SEARCH
    // =========================

    List<ProfileTypeResponseDTO> search(String keyword);

    List<ProfileTypeResponseDTO> searchByAdmin(Long adminId, String keyword);
}