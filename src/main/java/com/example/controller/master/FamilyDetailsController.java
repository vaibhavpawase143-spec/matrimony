package com.example.controller.master;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FamilyDetailsResponseDto;
import com.example.service.FamilyDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family-details")
@RequiredArgsConstructor
public class FamilyDetailsController {

    private final FamilyDetailsService familyDetailsService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FamilyDetailsResponseDto> create(
            @Valid @RequestBody FamilyDetailsRequestDto requestDto) {

        return ApiResponse.success(
                "Family Details created successfully.",
                familyDetailsService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FamilyDetailsResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FamilyDetailsRequestDto requestDto) {

        return ApiResponse.success(
                "Family Details updated successfully.",
                familyDetailsService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        familyDetailsService.softDelete(id);

        return ApiResponse.success(
                "Family Details deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        familyDetailsService.restore(id);

        return ApiResponse.success(
                "Family Details restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        familyDetailsService.hardDelete(id);

        return ApiResponse.success(
                "Family Details permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FamilyDetailsResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FamilyDetailsResponseDto>> getAll() {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FamilyDetailsResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted Family Details fetched successfully.",
                familyDetailsService.getDeleted()
        );
    }

    // =========================
    // GET BY PROFILE
    // =========================

    @GetMapping("/profile/{profileId}")
    public ApiResponse<FamilyDetailsResponseDto> getByProfile(
            @PathVariable Long profileId) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getByProfile(profileId)
        );
    }

    // =========================
    // EXISTS BY PROFILE
    // =========================

    @GetMapping("/profile/{profileId}/exists")
    public ApiResponse<Boolean> existsByProfile(
            @PathVariable Long profileId) {

        return ApiResponse.success(
                "Profile check completed.",
                familyDetailsService.existsByProfile(profileId)
        );
    }

    // =========================
    // GET ACTIVE BY PROFILE
    // =========================

    @GetMapping("/profile/{profileId}/active")
    public ApiResponse<List<FamilyDetailsResponseDto>> getActiveByProfile(
            @PathVariable Long profileId) {

        return ApiResponse.success(
                "Active Family Details fetched successfully.",
                familyDetailsService.getActiveByProfile(profileId)
        );
    }

    // =========================
    // GET BY FAMILY TYPE
    // =========================

    @GetMapping("/family-type/{familyTypeId}")
    public ApiResponse<List<FamilyDetailsResponseDto>> getByFamilyType(
            @PathVariable Long familyTypeId) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getByFamilyType(familyTypeId)
        );
    }

    // =========================
    // GET BY FAMILY
    // =========================

    @GetMapping("/family/{familyId}")
    public ApiResponse<List<FamilyDetailsResponseDto>> getByFamily(
            @PathVariable Long familyId) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getByFamily(familyId)
        );
    }

    // =========================
    // GET ACTIVE BY FAMILY
    // =========================

    @GetMapping("/family/{familyId}/active")
    public ApiResponse<List<FamilyDetailsResponseDto>> getActiveByFamily(
            @PathVariable Long familyId) {

        return ApiResponse.success(
                "Active Family Details fetched successfully.",
                familyDetailsService.getActiveByFamily(familyId)
        );
    }

    // =========================
    // GET BY BROTHER TYPE
    // =========================

    @GetMapping("/brother-type/{brotherTypeId}")
    public ApiResponse<List<FamilyDetailsResponseDto>> getByBrotherType(
            @PathVariable Long brotherTypeId) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getByBrotherType(brotherTypeId)
        );
    }

    // =========================
    // GET BY SISTER TYPE
    // =========================

    @GetMapping("/sister-type/{sisterTypeId}")
    public ApiResponse<List<FamilyDetailsResponseDto>> getBySisterType(
            @PathVariable Long sisterTypeId) {

        return ApiResponse.success(
                "Family Details fetched successfully.",
                familyDetailsService.getBySisterType(sisterTypeId)
        );
    }

}