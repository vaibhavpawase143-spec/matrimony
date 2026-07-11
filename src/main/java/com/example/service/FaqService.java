package com.example.service;

import com.example.dto.request.FaqRequestDTO;
import com.example.dto.response.FaqResponseDTO;

import java.util.List;

public interface FaqService {

    /**
     * Create FAQ
     */
    FaqResponseDTO create(FaqRequestDTO dto);

    /**
     * Update FAQ
     */
    FaqResponseDTO update(Long id, FaqRequestDTO dto);

    /**
     * Get FAQ by ID (Admin)
     */
    FaqResponseDTO getById(Long id);

    /**
     * Get all FAQs (Admin)
     */
    List<FaqResponseDTO> getAll();

    /**
     * Get all published FAQs (Public)
     */
    List<FaqResponseDTO> getPublishedFaqs();

    /**
     * Publish FAQ
     */
    void publish(Long id);

    /**
     * Unpublish FAQ
     */
    void unpublish(Long id);

    /**
     * Soft Delete FAQ
     */
    void delete(Long id);
}