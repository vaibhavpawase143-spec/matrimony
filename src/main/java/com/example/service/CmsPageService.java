package com.example.service;

import com.example.dto.request.CmsPageRequestDTO;
import com.example.dto.response.CmsPageResponseDTO;

import java.util.List;

public interface CmsPageService {

    /**
     * Create CMS Page
     */
    CmsPageResponseDTO create(CmsPageRequestDTO dto);

    /**
     * Update CMS Page
     */
    CmsPageResponseDTO update(Long id, CmsPageRequestDTO dto);

    /**
     * Get CMS Page by Id
     */
    CmsPageResponseDTO getById(Long id);

    /**
     * Get CMS Page by Page Key
     */
    CmsPageResponseDTO getByPageKey(String pageKey);

    /**
     * Get All CMS Pages (Admin)
     */
    List<CmsPageResponseDTO> getAll();

    /**
     * Publish CMS Page
     */
    void publish(Long id);

    /**
     * Unpublish CMS Page
     */
    void unpublish(Long id);

    /**
     * Soft Delete CMS Page
     */
    void delete(Long id);
}