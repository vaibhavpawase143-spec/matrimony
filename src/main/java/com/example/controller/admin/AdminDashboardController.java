package com.example.controller.admin;

import com.example.dto.response.AdminDashboardDTO;
import com.example.dto.response.ApiResponse;
import com.example.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminDashboardDTO> getDashboardOverview() {

        return ApiResponse.<AdminDashboardDTO>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(service.getDashboardOverview())
                .build();
    }
}