package com.example.controller.master;

import com.example.dto.request.DrinkingRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.DrinkingResponseDto;
import com.example.model.Admin;
import com.example.model.Drinking;
import com.example.service.DrinkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/drinking")
@RequiredArgsConstructor
public class DrinkingController {

    private final DrinkingService drinkingService;

    // ================= CREATE =================

    @PostMapping
    public ApiResponse<DrinkingResponseDto> create(
            @Valid @RequestBody DrinkingRequestDto dto) {

        Drinking saved = drinkingService.create(mapToEntity(dto));

        return ApiResponse.<DrinkingResponseDto>builder()
                .success(true)
                .message("Drinking created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ApiResponse<DrinkingResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DrinkingRequestDto dto) {

        Drinking updated = drinkingService.update(id, mapToEntity(dto));

        return ApiResponse.<DrinkingResponseDto>builder()
                .success(true)
                .message("Drinking updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        drinkingService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Drinking deleted successfully.")
                .build();
    }

    // ================= HARD DELETE =================

    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        drinkingService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Drinking permanently deleted successfully.")
                .build();
    }

    // ================= RESTORE =================

    @PutMapping("/restore/{id}")
    public ApiResponse<DrinkingResponseDto> restore(
            @PathVariable Long id) {

        Drinking restored = drinkingService.restore(id);

        return ApiResponse.<DrinkingResponseDto>builder()
                .success(true)
                .message("Drinking restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ApiResponse<DrinkingResponseDto> getById(
            @PathVariable Long id) {

        return ApiResponse.<DrinkingResponseDto>builder()
                .success(true)
                .message("Drinking retrieved successfully.")
                .data(mapToResponse(drinkingService.getById(id)))
                .build();
    }

    // ================= GET ALL =================

    @GetMapping
    public ApiResponse<List<DrinkingResponseDto>> getAll() {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Drinkings retrieved successfully.")
                .data(
                        drinkingService.getAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= GET ACTIVE =================

    @GetMapping("/active")
    public ApiResponse<List<DrinkingResponseDto>> getActive() {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Active drinking records retrieved successfully.")
                .data(
                        drinkingService.getActive()
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= GET INACTIVE =================

    @GetMapping("/inactive")
    public ApiResponse<List<DrinkingResponseDto>> getInactive() {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Inactive drinking records retrieved successfully.")
                .data(
                        drinkingService.getInactive()
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= GET DELETED =================

    @GetMapping("/deleted")
    public ApiResponse<List<DrinkingResponseDto>> getDeleted() {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Deleted drinking records retrieved successfully.")
                .data(
                        drinkingService.getDeleted()
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }
    // ================= GET BY ADMIN =================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<DrinkingResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Drinking records retrieved successfully.")
                .data(
                        drinkingService.getByAdmin(adminId)
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= GET ACTIVE BY ADMIN =================

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<DrinkingResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Active drinking records retrieved successfully.")
                .data(
                        drinkingService.getActiveByAdmin(adminId)
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= SEARCH =================

    @GetMapping("/search")
    public ApiResponse<List<DrinkingResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Search completed successfully.")
                .data(
                        drinkingService.search(keyword)
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= SEARCH BY ADMIN =================

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<DrinkingResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.<List<DrinkingResponseDto>>builder()
                .success(true)
                .message("Search completed successfully.")
                .data(
                        drinkingService.searchByAdmin(adminId, keyword)
                                .stream()
                                .map(this::mapToResponse)
                                .toList()
                )
                .build();
    }

    // ================= MAPPERS =================

    private Drinking mapToEntity(DrinkingRequestDto dto) {

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        return Drinking.builder()
                .name(dto.getName())
                .value(dto.getValue())
                .isActive(
                        dto.getIsActive() != null
                                ? dto.getIsActive()
                                : true
                )
                .admin(admin)
                .build();
    }

    private DrinkingResponseDto mapToResponse(Drinking entity) {

        DrinkingResponseDto dto = new DrinkingResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}