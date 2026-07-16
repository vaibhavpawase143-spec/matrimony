package com.example.controller.master;

import com.example.dto.request.MotherTongueRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.MotherTongueResponseDTO;
import com.example.service.MotherTongueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/mother-tongues")
@RequiredArgsConstructor
public class MotherTongueController {

    private final MotherTongueService motherTongueService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MotherTongueResponseDTO> create(
            @Valid @RequestBody MotherTongueRequestDTO requestDto) {

        return ApiResponse.success(
                "Mother Tongue created successfully.",
                motherTongueService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<MotherTongueResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MotherTongueRequestDTO requestDto) {

        return ApiResponse.success(
                "Mother Tongue updated successfully.",
                motherTongueService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        motherTongueService.softDelete(id);

        return ApiResponse.success(
                "Mother Tongue deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        motherTongueService.restore(id);

        return ApiResponse.success(
                "Mother Tongue restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        motherTongueService.hardDelete(id);

        return ApiResponse.success(
                "Mother Tongue permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<MotherTongueResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Mother Tongue fetched successfully.",
                motherTongueService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<MotherTongueResponseDTO>> getAll() {

        return ApiResponse.success(
                "Mother Tongue list fetched successfully.",
                motherTongueService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<MotherTongueResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Mother Tongue list fetched successfully.",
                motherTongueService.getDeleted()
        );
    }

    // =========================
    // ACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<MotherTongueResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Mother Tongue list fetched successfully.",
                motherTongueService.getActive()
        );
    }

    // =========================
    // INACTIVE
    // =========================

    @GetMapping("/inactive")
    public ApiResponse<List<MotherTongueResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Mother Tongue list fetched successfully.",
                motherTongueService.getInactive()
        );
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<MotherTongueResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Mother Tongue list fetched successfully.",
                motherTongueService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<MotherTongueResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Mother Tongue list fetched successfully.",
                motherTongueService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<MotherTongueResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Mother Tongue list fetched successfully.",
                motherTongueService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<MotherTongueResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                motherTongueService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<MotherTongueResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                motherTongueService.searchByAdmin(adminId, keyword)
        );
    }
}