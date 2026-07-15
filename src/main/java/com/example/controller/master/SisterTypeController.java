package com.example.controller.master;

import com.example.dto.request.SisterTypeRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SisterTypeResponseDTO;
import com.example.service.SisterTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/sister-types")
@RequiredArgsConstructor
public class SisterTypeController {

    private final SisterTypeService sisterTypeService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SisterTypeResponseDTO> create(
            @Valid @RequestBody SisterTypeRequestDTO requestDto) {

        return ApiResponse.success(
                "Sister Type created successfully.",
                sisterTypeService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<SisterTypeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SisterTypeRequestDTO requestDto) {

        return ApiResponse.success(
                "Sister Type updated successfully.",
                sisterTypeService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        sisterTypeService.softDelete(id);

        return ApiResponse.success(
                "Sister Type deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        sisterTypeService.restore(id);

        return ApiResponse.success(
                "Sister Type restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        sisterTypeService.hardDelete(id);

        return ApiResponse.success(
                "Sister Type permanently deleted successfully.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<SisterTypeResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Sister Type fetched successfully.",
                sisterTypeService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<SisterTypeResponseDTO>> getAll() {

        return ApiResponse.success(
                "Sister Type list fetched successfully.",
                sisterTypeService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<SisterTypeResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Sister Type list fetched successfully.",
                sisterTypeService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<SisterTypeResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Sister Type list fetched successfully.",
                sisterTypeService.getActive()
        );
    }

    // =====================================================
    // INACTIVE
    // =====================================================

    @GetMapping("/inactive")
    public ApiResponse<List<SisterTypeResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Sister Type list fetched successfully.",
                sisterTypeService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<SisterTypeResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Sister Type list fetched successfully.",
                sisterTypeService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<SisterTypeResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Sister Type list fetched successfully.",
                sisterTypeService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<SisterTypeResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Sister Type list fetched successfully.",
                sisterTypeService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<SisterTypeResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                sisterTypeService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<SisterTypeResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                sisterTypeService.searchByAdmin(adminId, keyword)
        );
    }
}