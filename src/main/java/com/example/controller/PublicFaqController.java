package com.example.controller;

import com.example.dto.response.ApiResponse;
import com.example.dto.response.FaqResponseDTO;
import com.example.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class PublicFaqController {

    private final FaqService faqService;

    @GetMapping
    public ApiResponse<List<FaqResponseDTO>> getPublishedFaqs() {

        return new ApiResponse<>(
                true,
                "FAQs retrieved successfully",
                faqService.getPublishedFaqs()
        );
    }
}