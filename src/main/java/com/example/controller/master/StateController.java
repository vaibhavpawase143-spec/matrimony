package com.example.controller.master;

import com.example.dto.request.StateRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.StateResponseDTO;
import com.example.service.StateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ApiResponse<StateResponseDTO> create(
            @Valid @RequestBody StateRequestDTO requestDto) {

        return ApiResponse.success(
                "State created successfully.",
                stateService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<StateResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StateRequestDTO requestDto) {

        return ApiResponse.success(
                "State updated successfully.",
                stateService.update(id, requestDto)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<StateResponseDTO> getById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "State fetched successfully.",
                stateService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<StateResponseDTO>> getAll() {

        return ApiResponse.success(
                "States fetched successfully.",
                stateService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<StateResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted states fetched successfully.",
                stateService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<StateResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active states fetched successfully.",
                stateService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<StateResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive states fetched successfully.",
                stateService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<StateResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "States fetched successfully.",
                stateService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<StateResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active states fetched successfully.",
                stateService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<StateResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive states fetched successfully.",
                stateService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // COUNTRY
    // =====================================================

    @GetMapping("/country/{countryId}/admin/{adminId}")
    public ApiResponse<List<StateResponseDTO>> getByCountryAndAdmin(
            @PathVariable Long countryId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "States fetched successfully.",
                stateService.getByCountryAndAdmin(countryId, adminId)
        );
    }

    @GetMapping("/country/{countryId}/admin/{adminId}/active")
    public ApiResponse<List<StateResponseDTO>> getActiveByCountryAndAdmin(
            @PathVariable Long countryId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active states fetched successfully.",
                stateService.getActiveByCountryAndAdmin(countryId, adminId)
        );
    }

    @GetMapping("/country/{countryId}/admin/{adminId}/inactive")
    public ApiResponse<List<StateResponseDTO>> getInactiveByCountryAndAdmin(
            @PathVariable Long countryId,
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive states fetched successfully.",
                stateService.getInactiveByCountryAndAdmin(countryId, adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<StateResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                stateService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<StateResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                stateService.searchByAdmin(adminId, keyword)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(
            @PathVariable Long id) {

        stateService.softDelete(id);

        return ApiResponse.success(
                "State deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PutMapping("/{id}/restore")
    public ApiResponse<Void> restore(
            @PathVariable Long id) {

        stateService.restore(id);

        return ApiResponse.success(
                "State restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(
            @PathVariable Long id) {

        stateService.hardDelete(id);

        return ApiResponse.success(
                "State permanently deleted successfully.",
                null
        );
    }
}