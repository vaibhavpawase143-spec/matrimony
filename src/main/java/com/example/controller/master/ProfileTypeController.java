package com.example.controller.master;

import com.example.dto.request.ProfileTypeRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ProfileTypeResponseDTO;
import com.example.service.ProfileTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/profile-types")
@RequiredArgsConstructor
public class ProfileTypeController {

    private final ProfileTypeService profileTypeService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileTypeResponseDTO> create(
            @Valid @RequestBody ProfileTypeRequestDTO requestDto) {

        return ApiResponse.success(
                "Profile Type created successfully.",
                profileTypeService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<ProfileTypeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProfileTypeRequestDTO requestDto) {

        return ApiResponse.success(
                "Profile Type updated successfully.",
                profileTypeService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        profileTypeService.softDelete(id);

        return ApiResponse.success(
                "Profile Type deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        profileTypeService.restore(id);

        return ApiResponse.success(
                "Profile Type restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        profileTypeService.hardDelete(id);

        return ApiResponse.success(
                "Profile Type permanently deleted.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<ProfileTypeResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Profile Type fetched successfully.",
                profileTypeService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<ProfileTypeResponseDTO>> getAll() {

        return ApiResponse.success(
                "Profile Type list fetched successfully.",
                profileTypeService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<ProfileTypeResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Profile Type list fetched successfully.",
                profileTypeService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<ProfileTypeResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Profile Type list fetched successfully.",
                profileTypeService.getActive()
        );
    }

    // =====================================================
    // INACTIVE
    // =====================================================

    @GetMapping("/inactive")
    public ApiResponse<List<ProfileTypeResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Profile Type list fetched successfully.",
                profileTypeService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<ProfileTypeResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Profile Type list fetched successfully.",
                profileTypeService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<ProfileTypeResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Profile Type list fetched successfully.",
                profileTypeService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<ProfileTypeResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Profile Type list fetched successfully.",
                profileTypeService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<ProfileTypeResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                profileTypeService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<ProfileTypeResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                profileTypeService.searchByAdmin(adminId, keyword)
        );
    }
}