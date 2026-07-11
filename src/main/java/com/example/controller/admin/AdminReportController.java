package com.example.controller.admin;

import com.example.dto.request.ReportFilterDTO;
import com.example.dto.response.AdminReportResponseDTO;
import com.example.dto.response.AdminReportStatsDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.ReportStatus;
import com.example.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    // ================= GET REPORTS =================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<AdminReportResponseDTO>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) String reporterName,
            @RequestParam(required = false) String reportedUserName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {

        ReportFilterDTO filter = ReportFilterDTO.builder()
                .search(search)
                .status(status)
                .reporterName(reporterName)
                .reportedUserName(reportedUserName)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return new ApiResponse<>(
                true,
                "All reports retrieved",
                adminReportService.getAllReports(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminReportResponseDTO> getReportById(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                true,
                "Report retrieved",
                adminReportService.getReportById(id)
        );
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<AdminReportResponseDTO>> getPendingReports() {
        return new ApiResponse<>(
                true,
                "Pending reports retrieved",
                adminReportService.getPendingReports()
        );
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminReportStatsDTO> getReportStatistics() {
        return new ApiResponse<>(
                true,
                "Report statistics retrieved",
                adminReportService.getReportStatistics()
        );
    }

    // ================= REPORT OPERATIONS =================

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminReportResponseDTO> markAsReviewed(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                true,
                "Report marked as under review",
                adminReportService.markAsReviewed(id)
        );
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminReportResponseDTO> approveReport(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                true,
                "Report approved successfully",
                adminReportService.approveReport(id)
        );
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminReportResponseDTO> rejectReport(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                true,
                "Report rejected successfully",
                adminReportService.rejectReport(id)
        );
    }
}
