package com.example.controller.master;

import com.example.dto.request.QualificationRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.QualificationResponseDTO;
import com.example.service.QualificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/qualifications")
@RequiredArgsConstructor
public class QualificationController {

    private final QualificationService qualificationService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QualificationResponseDTO> create(
            @Valid @RequestBody QualificationRequestDTO requestDto) {

        return ApiResponse.success(
                "Qualification created successfully.",
                qualificationService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<QualificationResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody QualificationRequestDTO requestDto) {

        return ApiResponse.success(
                "Qualification updated successfully.",
                qualificationService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        qualificationService.softDelete(id);

        return ApiResponse.success(
                "Qualification deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        qualificationService.restore(id);

        return ApiResponse.success(
                "Qualification restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        qualificationService.hardDelete(id);

        return ApiResponse.success(
                "Qualification permanently deleted.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<QualificationResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Qualification fetched successfully.",
                qualificationService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<QualificationResponseDTO>> getAll() {

        return ApiResponse.success(
                "Qualification list fetched successfully.",
                qualificationService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<QualificationResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Qualification list fetched successfully.",
                qualificationService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<QualificationResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Qualification list fetched successfully.",
                qualificationService.getActive()
        );
    }

    // =====================================================
    // INACTIVE
    // =====================================================

    @GetMapping("/inactive")
    public ApiResponse<List<QualificationResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Qualification list fetched successfully.",
                qualificationService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<QualificationResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Qualification list fetched successfully.",
                qualificationService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<QualificationResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Qualification list fetched successfully.",
                qualificationService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<QualificationResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Qualification list fetched successfully.",
                qualificationService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<QualificationResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                qualificationService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<QualificationResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                qualificationService.searchByAdmin(adminId, keyword)
        );
    }
}