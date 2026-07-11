package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.CmsPageResponseDTO;
import com.example.service.CmsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cms")
@RequiredArgsConstructor
public class PublicCmsController {

    private final CmsPageService cmsPageService;

    @GetMapping("/{pageKey}")
    public ApiResponse<CmsPageResponseDTO> getPage(
            @PathVariable String pageKey
    ) {

        return new ApiResponse<>(
                true,
                "CMS page retrieved successfully",
                cmsPageService.getByPageKey(pageKey)
        );
    }
}