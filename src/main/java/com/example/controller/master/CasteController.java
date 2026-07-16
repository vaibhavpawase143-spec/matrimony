package com.example.controller.master;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CasteResponseDTO;
import com.example.model.Caste;
import com.example.model.Religion;
import com.example.service.CasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins/{adminId}/castes")
@RequiredArgsConstructor
public class CasteController {

    private final CasteService casteService;

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<CasteResponseDTO> create(
            @PathVariable Long adminId,
            @Valid @RequestBody CasteRequestDTO dto) {

        Caste saved = casteService.create(
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<CasteResponseDTO>builder()
                .success(true)
                .message("Caste created successfully.")
                .data(mapToResponse(saved))
                .build();
    }
    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<CasteResponseDTO> getById(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        Caste caste = casteService.getById(id, adminId);

        return ApiResponse.<CasteResponseDTO>builder()
                .success(true)
                .message("Caste retrieved successfully.")
                .data(mapToResponse(caste))
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<CasteResponseDTO>> getAll(
            @PathVariable Long adminId) {

        List<CasteResponseDTO> castes = casteService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Castes retrieved successfully.")
                .data(castes)
                .build();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<CasteResponseDTO>> getActive(
            @PathVariable Long adminId) {

        List<CasteResponseDTO> activeCastes = casteService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Active castes retrieved successfully.")
                .data(activeCastes)
                .build();
    }
    // ================= GET BY RELIGION =================
    @GetMapping("/religion/{religionId}")
    public ApiResponse<List<CasteResponseDTO>> getByReligion(
            @PathVariable Long adminId,
            @PathVariable Long religionId) {

        List<CasteResponseDTO> castes = casteService.getByReligion(religionId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Castes retrieved successfully by religion.")
                .data(castes)
                .build();
    }

    // ================= GET ACTIVE BY RELIGION =================
    @GetMapping("/religion/{religionId}/active")
    public ApiResponse<List<CasteResponseDTO>> getActiveByReligion(
            @PathVariable Long adminId,
            @PathVariable Long religionId) {

        List<CasteResponseDTO> activeCastes = casteService
                .getActiveByReligion(religionId, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Active castes retrieved successfully by religion.")
                .data(activeCastes)
                .build();
    }
    // ================= SEARCH =================
    @GetMapping("/search")
    public ApiResponse<List<CasteResponseDTO>> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        List<CasteResponseDTO> castes = casteService.search(keyword, adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Castes searched successfully.")
                .data(castes)
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<CasteResponseDTO> update(
            @PathVariable Long adminId,
            @PathVariable Long id,
            @Valid @RequestBody CasteRequestDTO dto) {

        Caste updated = casteService.update(
                id,
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<CasteResponseDTO>builder()
                .success(true)
                .message("Caste updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }
    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        casteService.delete(id, adminId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Caste deleted successfully.")
                .build();
    }
    // ================= MAPPERS =================

    private Caste mapToEntity(CasteRequestDTO dto) {

        Caste caste = new Caste();

        caste.setName(dto.getName());

        // 👇 Important: set only ID (Service will fetch full entity)
        Religion religion = new Religion();
        religion.setId(dto.getReligionId());
        caste.setReligion(religion);

        caste.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return caste;
    }

    private CasteResponseDTO mapToResponse(Caste caste) {

        CasteResponseDTO dto = new CasteResponseDTO();

        dto.setId(caste.getId());
        dto.setName(caste.getName());
        dto.setIsActive(caste.getIsActive());
        dto.setCreatedAt(caste.getCreatedAt());

        if (caste.getAdmin() != null) {
            dto.setAdminId(caste.getAdmin().getId());
        }

        if (caste.getReligion() != null) {
            dto.setReligionId(caste.getReligion().getId());
            dto.setReligionName(caste.getReligion().getName());
        }

        return dto;
    }
    @GetMapping("/deleted")
    public ApiResponse<List<CasteResponseDTO>> getDeleted(
            @PathVariable Long adminId) {

        List<CasteResponseDTO> deleted = casteService.getDeleted(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CasteResponseDTO>>builder()
                .success(true)
                .message("Deleted castes retrieved successfully.")
                .data(deleted)
                .build();
    }
    @PutMapping("/restore/{id}")
    public ApiResponse<CasteResponseDTO> restore(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        Caste restored = casteService.restore(id);

        return ApiResponse.<CasteResponseDTO>builder()
                .success(true)
                .message("Caste restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        casteService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Caste permanently deleted successfully.")
                .build();
    }
    
}