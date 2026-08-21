package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.SuccessStoryResponseDTO;
import com.example.service.SuccessStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/success-stories")
@RequiredArgsConstructor
public class SuccessStoryController {

    private final SuccessStoryService successStoryService;

    // ================= GET PUBLISHED STORIES (PUBLIC) =================

    @GetMapping
    public ApiResponse<Page<SuccessStoryResponseDTO>> getPublishedStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        int maxPageSize = Math.min(Math.max(1, size), 50);
        Sort sort = Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, maxPageSize, sort);

        Page<SuccessStoryResponseDTO> stories = successStoryService.getPublishedSuccessStories(pageable);
        return new ApiResponse<>(true, "Published success stories retrieved successfully", stories);
    }

    // ================= GET PUBLISHED STORY BY ID (PUBLIC) =================

    @GetMapping("/{id}")
    public ApiResponse<SuccessStoryResponseDTO> getPublishedStoryById(@PathVariable Long id) {
        SuccessStoryResponseDTO story = successStoryService.getPublicSuccessStoryById(id);
        return new ApiResponse<>(true, "Success story retrieved successfully", story);
    }
}
