package com.example.mapper;

import com.example.dto.request.FaqRequestDTO;
import com.example.dto.response.FaqResponseDTO;
import com.example.model.Faq;

public class FaqMapper {

    private FaqMapper() {
    }

    /**
     * Request DTO -> Entity
     */
    public static Faq toEntity(FaqRequestDTO dto) {

        Faq faq = new Faq();

        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setDisplayOrder(dto.getDisplayOrder());
        faq.setPublished(dto.getPublished());
        faq.setIsActive(dto.getIsActive());

        return faq;
    }

    /**
     * Entity -> Response DTO
     */
    public static FaqResponseDTO toDTO(Faq faq) {

        FaqResponseDTO dto = new FaqResponseDTO();

        dto.setId(faq.getId());

        dto.setQuestion(faq.getQuestion());
        dto.setAnswer(faq.getAnswer());

        dto.setDisplayOrder(faq.getDisplayOrder());

        dto.setPublished(faq.getPublished());
        dto.setIsActive(faq.getIsActive());

        if (faq.getCreatedBy() != null) {
            dto.setCreatedById(faq.getCreatedBy().getId());
            dto.setCreatedByName(faq.getCreatedBy().getName());
        }

        if (faq.getUpdatedBy() != null) {
            dto.setUpdatedById(faq.getUpdatedBy().getId());
            dto.setUpdatedByName(faq.getUpdatedBy().getName());
        }

        dto.setCreatedAt(faq.getCreatedAt());
        dto.setUpdatedAt(faq.getUpdatedAt());

        return dto;
    }
}