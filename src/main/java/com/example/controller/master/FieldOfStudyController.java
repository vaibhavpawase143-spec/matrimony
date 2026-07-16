package com.example.controller.master;

import com.example.dto.request.FieldOfStudyRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FieldOfStudyResponseDTO;
import com.example.service.FieldOfStudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/fields-of-study")
@RequiredArgsConstructor
public class FieldOfStudyController {

    private final FieldOfStudyService fieldOfStudyService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FieldOfStudyResponseDTO> create(
            @Valid @RequestBody FieldOfStudyRequestDTO requestDto) {

        return ApiResponse.success(
                "Field Of Study created successfully.",
                fieldOfStudyService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FieldOfStudyResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FieldOfStudyRequestDTO requestDto) {

        return ApiResponse.success(
                "Field Of Study updated successfully.",
                fieldOfStudyService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        fieldOfStudyService.softDelete(id);

        return ApiResponse.success(
                "Field Of Study deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        fieldOfStudyService.restore(id);

        return ApiResponse.success(
                "Field Of Study restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        fieldOfStudyService.hardDelete(id);

        return ApiResponse.success(
                "Field Of Study permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FieldOfStudyResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Field Of Study fetched successfully.",
                fieldOfStudyService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FieldOfStudyResponseDTO>> getAll() {

        return ApiResponse.success(
                "Field Of Studies fetched successfully.",
                fieldOfStudyService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Field Of Studies fetched successfully.",
                fieldOfStudyService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Field Of Studies fetched successfully.",
                fieldOfStudyService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Field Of Studies fetched successfully.",
                fieldOfStudyService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Field Of Studies fetched successfully.",
                fieldOfStudyService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Field Of Studies fetched successfully.",
                fieldOfStudyService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<FieldOfStudyResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Field Of Studies fetched successfully.",
                fieldOfStudyService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<FieldOfStudyResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                fieldOfStudyService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<FieldOfStudyResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                fieldOfStudyService.searchByAdmin(adminId, keyword)
        );
    }
}