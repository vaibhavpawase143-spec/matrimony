package com.example.mapper;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;
import com.example.model.Admin;
import com.example.model.Religion;

/**
 * Mapper utility for Religion entity ↔ DTO conversions.
 * Handles entity to/from request and response DTOs with null safety.
 */
public class ReligionMapper {

    private ReligionMapper() {
        // Prevent instantiation
    }

    /**
     * Convert ReligionRequestDTO to Religion entity.
     *
     * @param dto Request DTO
     * @param admin Admin performing the operation
     * @return Religion entity
     */
    public static Religion toEntity(ReligionRequestDTO dto, Admin admin) {
        if (dto == null) {
            return null;
        }

        Religion religion = new Religion();
        religion.setName(dto.getName());
        religion.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        religion.setAdmin(admin);

        return religion;
    }

    /**
     * Convert Religion entity to ReligionResponseDTO.
     *
     * @param religion Religion entity
     * @return Response DTO
     */
    public static ReligionResponseDTO toResponseDTO(Religion religion) {
        if (religion == null) {
            return null;
        }

        return ReligionResponseDTO.builder()
                .id(religion.getId())
                .adminId(religion.getAdmin() != null ? religion.getAdmin().getId() : null)
                .adminName(religion.getAdmin() != null ? religion.getAdmin().getName() : null)
                .name(religion.getName())
                .isActive(religion.getIsActive())
                .createdAt(religion.getCreatedAt())
                .updatedAt(religion.getUpdatedAt())
                .build();
    }

    /**
     * Update existing Religion entity with DTO values.
     *
     * @param religion Existing entity
     * @param dto Request DTO
     */
    public static void updateEntity(Religion religion, ReligionRequestDTO dto) {
        if (religion == null || dto == null) {
            return;
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            religion.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            religion.setIsActive(dto.getIsActive());
        }
    }
}

