package com.example.controller.master;

import com.example.dto.request.SmokingRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SmokingResponseDTO;
import com.example.service.SmokingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/smoking")
@RequiredArgsConstructor
public class SmokingController {

    private final SmokingService smokingService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ApiResponse<SmokingResponseDTO> create(
            @Valid @RequestBody SmokingRequestDTO requestDto) {

        return ApiResponse.success(
                "Smoking created successfully.",
                smokingService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<SmokingResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SmokingRequestDTO requestDto) {

        return ApiResponse.success(
                "Smoking updated successfully.",
                smokingService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        smokingService.softDelete(id);

        return ApiResponse.success(
                "Smoking deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        smokingService.restore(id);

        return ApiResponse.success(
                "Smoking restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        smokingService.hardDelete(id);

        return ApiResponse.success(
                "Smoking permanently deleted.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<SmokingResponseDTO> getById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "Smoking fetched successfully.",
                smokingService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<SmokingResponseDTO>> getAll() {

        return ApiResponse.success(
                "Smoking list fetched successfully.",
                smokingService.getAll()
        );
    }

    @GetMapping("/deleted")
    public ApiResponse<List<SmokingResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted smoking list fetched successfully.",
                smokingService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<SmokingResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active smoking list fetched successfully.",
                smokingService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<SmokingResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive smoking list fetched successfully.",
                smokingService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<SmokingResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Smoking list fetched successfully.",
                smokingService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<SmokingResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active smoking list fetched successfully.",
                smokingService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<SmokingResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive smoking list fetched successfully.",
                smokingService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<SmokingResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                smokingService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<SmokingResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                smokingService.searchByAdmin(adminId, keyword)
        );
    }
}