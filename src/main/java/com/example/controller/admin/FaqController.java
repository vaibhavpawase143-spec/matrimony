package com.example.controller.admin;

import com.example.dto.request.FaqRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FaqResponseDTO;
import com.example.service.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // ================= CREATE =================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<FaqResponseDTO> create(
            @Valid @RequestBody FaqRequestDTO dto
    ) {

        return new ApiResponse<>(
                true,
                "FAQ created successfully",
                faqService.create(dto)
        );
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<FaqResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FaqRequestDTO dto
    ) {

        return new ApiResponse<>(
                true,
                "FAQ updated successfully",
                faqService.update(id, dto)
        );
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<FaqResponseDTO> getById(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "FAQ retrieved successfully",
                faqService.getById(id)
        );
    }

    // ================= GET ALL =================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<FaqResponseDTO>> getAll() {

        return new ApiResponse<>(
                true,
                "FAQs retrieved successfully",
                faqService.getAll()
        );
    }

    // ================= PUBLISH =================

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> publish(
            @PathVariable Long id
    ) {

        faqService.publish(id);

        return new ApiResponse<>(
                true,
                "FAQ published successfully",
                null
        );
    }

    // ================= UNPUBLISH =================

    @PatchMapping("/{id}/unpublish")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> unpublish(
            @PathVariable Long id
    ) {

        faqService.unpublish(id);

        return new ApiResponse<>(
                true,
                "FAQ unpublished successfully",
                null
        );
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> delete(
            @PathVariable Long id
    ) {

        faqService.delete(id);

        return new ApiResponse<>(
                true,
                "FAQ deleted successfully",
                null
        );
    }
}