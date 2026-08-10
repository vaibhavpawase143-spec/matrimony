package com.example.controller.user;

import com.example.dto.response.DashboardSummaryDTO;
import com.example.security.CustomUserDetails;
import com.example.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryDTO summary(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return dashboardService.getSummary(
                userDetails.getId()
        );
    }
}