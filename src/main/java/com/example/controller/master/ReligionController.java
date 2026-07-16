package com.example.controller.master;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ReligionResponseDTO;
import com.example.service.ReligionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/religions")
@RequiredArgsConstructor
public class ReligionController {

    private final ReligionService religionService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReligionResponseDTO> create(
            @Valid @RequestBody ReligionRequestDTO requestDto) {

        return ApiResponse.success(
                "Religion created successfully.",
                religionService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<ReligionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ReligionRequestDTO requestDto) {

        return ApiResponse.success(
                "Religion updated successfully.",
                religionService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        religionService.softDelete(id);

        return ApiResponse.success(
                "Religion deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        religionService.restore(id);

        return ApiResponse.success(
                "Religion restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        religionService.hardDelete(id);

        return ApiResponse.success(
                "Religion permanently deleted successfully.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<ReligionResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Religion fetched successfully.",
                religionService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<ReligionResponseDTO>> getAll() {

        return ApiResponse.success(
                "Religion list fetched successfully.",
                religionService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<ReligionResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted religion list fetched successfully.",
                religionService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<ReligionResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active religion list fetched successfully.",
                religionService.getActive()
        );
    }

    // =====================================================
    // INACTIVE
    // =====================================================

    @GetMapping("/inactive")
    public ApiResponse<List<ReligionResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive religion list fetched successfully.",
                religionService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<ReligionResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Religion list fetched successfully.",
                religionService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<ReligionResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active religion list fetched successfully.",
                religionService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<ReligionResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive religion list fetched successfully.",
                religionService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<ReligionResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                religionService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<ReligionResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                religionService.searchByAdmin(adminId, keyword)
        );
    }
}