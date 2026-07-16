package com.example.controller.master;

import com.example.dto.request.WeightRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.WeightResponseDTO;
import com.example.service.WeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/weights")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ApiResponse<WeightResponseDTO> create(
            @Valid @RequestBody WeightRequestDTO requestDto) {

        return ApiResponse.success(
                "Weight created successfully.",
                weightService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ApiResponse<WeightResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody WeightRequestDTO requestDto) {

        return ApiResponse.success(
                "Weight updated successfully.",
                weightService.update(id, requestDto)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ApiResponse<WeightResponseDTO> getById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "Weight fetched successfully.",
                weightService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<WeightResponseDTO>> getAll() {

        return ApiResponse.success(
                "Weights fetched successfully.",
                weightService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted weights fetched successfully.",
                weightService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<WeightResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active weights fetched successfully.",
                weightService.getActive()
        );
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive weights fetched successfully.",
                weightService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Weights fetched successfully.",
                weightService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active weights fetched successfully.",
                weightService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive weights fetched successfully.",
                weightService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<WeightResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                weightService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<WeightResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                weightService.searchByAdmin(adminId, keyword)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Void> softDelete(
            @PathVariable Long id) {

        weightService.softDelete(id);

        return ApiResponse.success(
                "Weight deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Void> restore(
            @PathVariable Long id) {

        weightService.restore(id);

        return ApiResponse.success(
                "Weight restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ApiResponse<Void> hardDelete(
            @PathVariable Long id) {

        weightService.hardDelete(id);

        return ApiResponse.success(
                "Weight permanently deleted successfully.",
                null
        );
    }
}