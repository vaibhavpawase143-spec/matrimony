package com.example.mapper;

import com.example.dto.request.CmsPageRequestDTO;
import com.example.dto.response.CmsPageResponseDTO;
import com.example.model.CmsPage;

public class CmsPageMapper {

    private CmsPageMapper() {
    }

    /**
     * Entity -> Response DTO
     */
    public static CmsPageResponseDTO toDTO(CmsPage page) {

        if (page == null) {
            return null;
        }

        CmsPageResponseDTO dto = new CmsPageResponseDTO();

        dto.setId(page.getId());
        dto.setPageKey(page.getPageKey());
        dto.setTitle(page.getTitle());
        dto.setContent(page.getContent());
        dto.setPublished(page.getPublished());
        dto.setIsActive(page.getIsActive());

        if (page.getCreatedBy() != null) {
            dto.setCreatedById(page.getCreatedBy().getId());
            dto.setCreatedByName(page.getCreatedBy().getName());
        }

        if (page.getUpdatedBy() != null) {
            dto.setUpdatedById(page.getUpdatedBy().getId());
            dto.setUpdatedByName(page.getUpdatedBy().getName());
        }

        dto.setCreatedAt(page.getCreatedAt());
        dto.setUpdatedAt(page.getUpdatedAt());

        return dto;
    }

    /**
     * Request DTO -> Entity
     */
    public static CmsPage toEntity(CmsPageRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        CmsPage page = new CmsPage();

        page.setPageKey(dto.getPageKey());
        page.setTitle(dto.getTitle());
        page.setContent(dto.getContent());

        if (dto.getPublished() != null) {
            page.setPublished(dto.getPublished());
        }

        if (dto.getIsActive() != null) {
            page.setIsActive(dto.getIsActive());
        }

        return page;
    }
}