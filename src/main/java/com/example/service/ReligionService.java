package com.example.service;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;

import java.util.List;

/**
 * Service interface for Religion master data.
 * Handles CRUD operations, soft delete, and search functionality.
 */
public interface ReligionService {

    // ================= CRUD OPERATIONS =================

    /**
     * Create or save a religion
     */
    ReligionResponseDTO create(ReligionRequestDTO dto, Long adminId);

    /**
     * Fetch religion by ID (non-deleted only)
     */
    ReligionResponseDTO getById(Long id);

    /**
     * Fetch all active religions
     */
    List<ReligionResponseDTO> getAll();

    /**
     * Update an existing religion
     */
    ReligionResponseDTO update(Long id, ReligionRequestDTO dto);

    /**
     * Soft delete a religion
     */
    void delete(Long id, Long deletedBy);

    /**
     * Permanently delete a religion (hard delete)
     */
    void hardDelete(Long id);

    // ================= ADMIN-SPECIFIC QUERIES =================

    /**
     * Get all religions by admin (non-deleted)
     */
    List<ReligionResponseDTO> getByAdmin(Long adminId);

    /**
     * Get active religions by admin
     */
    List<ReligionResponseDTO> getActiveByAdmin(Long adminId);

    /**
     * Get inactive religions by admin (non-deleted)
     */
    List<ReligionResponseDTO> getInactiveByAdmin(Long adminId);

    /**
     * Search religions by admin
     */
    List<ReligionResponseDTO> searchByAdmin(Long adminId, String keyword);

    /**
     * Get deleted religions by admin
     */
    List<ReligionResponseDTO> getDeletedByAdmin(Long adminId);

    /**
     * Check if religion exists for admin
     */
    boolean existsForAdmin(String name, Long adminId);

    /**
     * Restore soft-deleted religion
     */
    ReligionResponseDTO restore(Long id);
}