package com.example.controller.master;

import com.example.dto.request.EducationLevelRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.EducationLevelResponseDto;
import com.example.service.EducationLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/education-levels")
@RequiredArgsConstructor
public class EducationLevelController {

    private final EducationLevelService educationLevelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EducationLevelResponseDto> create(
            @Valid @RequestBody EducationLevelRequestDto requestDto) {

        return ApiResponse.success(
                "Education Level created successfully.",
                educationLevelService.create(requestDto)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<EducationLevelResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EducationLevelRequestDto requestDto) {

        return ApiResponse.success(
                "Education Level updated successfully.",
                educationLevelService.update(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        educationLevelService.softDelete(id);

        return ApiResponse.success(
                "Education Level deleted successfully.",
                null
        );
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        educationLevelService.restore(id);

        return ApiResponse.success(
                "Education Level restored successfully.",
                null
        );
    }

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        educationLevelService.hardDelete(id);

        return ApiResponse.success(
                "Education Level permanently deleted.",
                null
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<EducationLevelResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Education Level fetched successfully.",
                educationLevelService.getById(id)
        );
    }

    @GetMapping
    public ApiResponse<List<EducationLevelResponseDto>> getAll() {

        return ApiResponse.success(
                "Education Levels fetched successfully.",
                educationLevelService.getAll()
        );
    }

    @GetMapping("/deleted")
    public ApiResponse<List<EducationLevelResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted Education Levels fetched successfully.",
                educationLevelService.getDeleted()
        );
    }

    @GetMapping("/active")
    public ApiResponse<List<EducationLevelResponseDto>> getActive() {

        return ApiResponse.success(
                "Active Education Levels fetched successfully.",
                educationLevelService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<EducationLevelResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive Education Levels fetched successfully.",
                educationLevelService.getInactive()
        );
    }

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<EducationLevelResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Education Levels fetched successfully.",
                educationLevelService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<EducationLevelResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Education Levels fetched successfully.",
                educationLevelService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<EducationLevelResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Education Levels fetched successfully.",
                educationLevelService.getInactiveByAdmin(adminId)
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<EducationLevelResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                educationLevelService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<EducationLevelResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                educationLevelService.searchByAdmin(adminId, keyword)
        );
    }
}