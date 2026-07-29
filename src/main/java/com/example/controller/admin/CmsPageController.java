package com.example.controller.admin;

import com.example.dto.request.CmsPageRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CmsPageResponseDTO;
import com.example.service.CmsPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cms")
@RequiredArgsConstructor
public class CmsPageController {

    private final CmsPageService cmsPageService;

    // ================= CREATE =================

    @PostMapping
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<CmsPageResponseDTO> create(
            @Valid @RequestBody CmsPageRequestDTO dto
    ) {

        return new ApiResponse<>(
                true,
                "CMS page created successfully",
                cmsPageService.create(dto)
        );
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<CmsPageResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CmsPageRequestDTO dto
    ) {

        return new ApiResponse<>(
                true,
                "CMS page updated successfully",
                cmsPageService.update(id, dto)
        );
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<CmsPageResponseDTO> getById(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "CMS page retrieved successfully",
                cmsPageService.getById(id)
        );
    }

    // ================= GET ALL =================

    @GetMapping
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<List<CmsPageResponseDTO>> getAll() {

        return new ApiResponse<>(
                true,
                "CMS pages retrieved successfully",
                cmsPageService.getAll()
        );
    }

    // ================= PUBLISH =================

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<String> publish(
            @PathVariable Long id
    ) {

        cmsPageService.publish(id);

        return new ApiResponse<>(
                true,
                "CMS page published successfully",
                null
        );
    }

    // ================= UNPUBLISH =================

    @PatchMapping("/{id}/unpublish")
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<String> unpublish(
            @PathVariable Long id
    ) {

        cmsPageService.unpublish(id);

        return new ApiResponse<>(
                true,
                "CMS page unpublished successfully",
                null
        );
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CMS_MANAGE')")
    public ApiResponse<String> delete(
            @PathVariable Long id
    ) {

        cmsPageService.delete(id);

        return new ApiResponse<>(
                true,
                "CMS page deleted successfully",
                null
        );
    }

}