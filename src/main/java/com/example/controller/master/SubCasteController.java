package com.example.controller.master;

import com.example.dto.request.SubCasteRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SubCasteResponseDTO;
import com.example.service.SubCasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/sub-castes")
@RequiredArgsConstructor
public class SubCasteController {

    private final SubCasteService subCasteService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ApiResponse<SubCasteResponseDTO> create(
            @Valid @RequestBody SubCasteRequestDTO requestDto) {

        return ApiResponse.success(
                "Sub Caste created successfully.",
                subCasteService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<SubCasteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SubCasteRequestDTO requestDto) {

        return ApiResponse.success(
                "Sub Caste updated successfully.",
                subCasteService.update(id, requestDto)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<SubCasteResponseDTO> getById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "Sub Caste fetched successfully.",
                subCasteService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<SubCasteResponseDTO>> getAll() {

        return ApiResponse.success(
                "Sub Castes fetched successfully.",
                subCasteService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<SubCasteResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Sub Castes fetched successfully.",
                subCasteService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<SubCasteResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Sub Castes fetched successfully.",
                subCasteService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<SubCasteResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Sub Castes fetched successfully.",
                subCasteService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<SubCasteResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Sub Castes fetched successfully.",
                subCasteService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<SubCasteResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Sub Castes fetched successfully.",
                subCasteService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<SubCasteResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Sub Castes fetched successfully.",
                subCasteService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // CASTE
    // =====================================================

    @GetMapping("/caste/{casteId}/admin/{adminId}")
    public ApiResponse<List<SubCasteResponseDTO>> getByCasteAndAdmin(
            @PathVariable Long casteId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Sub Castes fetched successfully.",
                subCasteService.getByCasteAndAdmin(casteId, adminId)
        );
    }

    @GetMapping("/caste/{casteId}/admin/{adminId}/active")
    public ApiResponse<List<SubCasteResponseDTO>> getActiveByCasteAndAdmin(
            @PathVariable Long casteId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Sub Castes fetched successfully.",
                subCasteService.getActiveByCasteAndAdmin(casteId, adminId)
        );
    }

    @GetMapping("/caste/{casteId}/admin/{adminId}/inactive")
    public ApiResponse<List<SubCasteResponseDTO>> getInactiveByCasteAndAdmin(
            @PathVariable Long casteId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Sub Castes fetched successfully.",
                subCasteService.getInactiveByCasteAndAdmin(casteId, adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<SubCasteResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                subCasteService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<SubCasteResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                subCasteService.searchByAdmin(adminId, keyword)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(
            @PathVariable Long id) {

        subCasteService.softDelete(id);

        return ApiResponse.success(
                "Sub Caste deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PutMapping("/{id}/restore")
    public ApiResponse<Void> restore(
            @PathVariable Long id) {

        subCasteService.restore(id);

        return ApiResponse.success(
                "Sub Caste restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(
            @PathVariable Long id) {

        subCasteService.hardDelete(id);

        return ApiResponse.success(
                "Sub Caste permanently deleted successfully.",
                null
        );
    }
}