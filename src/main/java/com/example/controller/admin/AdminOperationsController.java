//package com.example.controller.admin;
//
//import com.example.dto.request.ReportRequestDTO;
//import com.example.dto.response.AdminDashboardDTO;
//import com.example.dto.response.ApiResponse;
//import com.example.dto.response.ReportResponseDTO;
//import com.example.model.Report;
//import com.example.service.AdminAnalyticsService;
//import com.example.service.ReportService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin/operations")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class AdminOperationsController {
//
//    private final ReportService reportService;
//    private final AdminAnalyticsService adminAnalyticsService;
//
//    // ================= DASHBOARD & ANALYTICS =================
//
//    @GetMapping("/dashboard")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<AdminDashboardDTO> getDashboard() {
//        AdminDashboardDTO dashboard = adminAnalyticsService.getFullDashboard();
//        return new ApiResponse<>(true, "Dashboard data retrieved", dashboard);
//    }
//
//    @GetMapping("/dashboard/range")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<AdminDashboardDTO> getDashboardByRange(
//            @RequestParam String startDate,
//            @RequestParam String endDate) {
//        AdminDashboardDTO dashboard = adminAnalyticsService.getDashboardForDateRange(startDate, endDate);
//        return new ApiResponse<>(true, "Dashboard data for range retrieved", dashboard);
//    }
//
//    // ================= USER STATISTICS =================
//
//    @GetMapping("/stats/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Long>> getUserStats() {
//        Map<String, Long> stats = Map.of(
//                "totalUsers", adminAnalyticsService.getTotalUsers(),
//                "activeUsers", adminAnalyticsService.getActiveUsersCount(),
//                "inactiveUsers", adminAnalyticsService.getInactiveUsersCount(),
//                "verifiedUsers", adminAnalyticsService.getVerifiedUsersCount(),
//                "unverifiedUsers", adminAnalyticsService.getUnverifiedUsersCount(),
//                "newThisMonth", adminAnalyticsService.getNewUsersThisMonth(),
//                "newThisWeek", adminAnalyticsService.getNewUsersThisWeek(),
//                "newToday", adminAnalyticsService.getNewUsersToday()
//        );
//        return new ApiResponse<>(true, "User statistics retrieved", stats);
//    }
//
//    @GetMapping("/stats/payments")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Object>> getPaymentStats() {
//        Map<String, Object> stats = Map.of(
//                "totalRevenue", adminAnalyticsService.getTotalRevenue(),
//                "revenueThisMonth", adminAnalyticsService.getRevenueThisMonth(),
//                "revenueThisWeek", adminAnalyticsService.getRevenueThisWeek(),
//                "totalTransactions", adminAnalyticsService.getTotalTransactions(),
//                "successfulTransactions", adminAnalyticsService.getSuccessfulTransactions(),
//                "failedTransactions", adminAnalyticsService.getFailedTransactions(),
//                "pendingTransactions", adminAnalyticsService.getPendingTransactions()
//        );
//        return new ApiResponse<>(true, "Payment statistics retrieved", stats);
//    }
//
//    @GetMapping("/stats/subscriptions")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Long>> getSubscriptionStats() {
//        Map<String, Long> stats = Map.of(
//                "activeSubscriptions", adminAnalyticsService.getActiveSubscriptions(),
//                "expiredSubscriptions", adminAnalyticsService.getExpiredSubscriptions(),
//                "totalSubscriptions", adminAnalyticsService.getTotalSubscriptions()
//        );
//        return new ApiResponse<>(true, "Subscription statistics retrieved", stats);
//    }
//
//    @GetMapping("/stats/growth")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Double>> getGrowthStats() {
//        Map<String, Double> stats = Map.of(
//                "userGrowth", adminAnalyticsService.getUserGrowthPercentage(),
//                "revenueGrowth", adminAnalyticsService.getRevenueGrowthPercentage(),
//                "subscriptionGrowth", adminAnalyticsService.getSubscriptionGrowthPercentage()
//        );
//        return new ApiResponse<>(true, "Growth statistics retrieved", stats);
//    }
//
//    // ================= REPORT MANAGEMENT =================
//
//    @PostMapping("/reports")
//    @PreAuthorize("hasRole('USER')")
//    public ApiResponse<ReportResponseDTO> createReport(@Valid @RequestBody ReportRequestDTO dto) {
//        ReportResponseDTO report = reportService.createReport(
//                dto.getReportedUserId(),
//                dto.getReason(),
//                dto.getDescription(),
//                dto.getReportType()
//        );
//        return new ApiResponse<>(true, "Report created successfully", report);
//    }
//
//    @GetMapping("/reports")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<ReportResponseDTO>> getAllReports(Pageable pageable) {
//        Page<ReportResponseDTO> reports = reportService.getAllReports(pageable);
//        return new ApiResponse<>(true, "Reports retrieved", reports);
//    }
//
//    @GetMapping("/reports/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<ReportResponseDTO> getReportById(@PathVariable Long id) {
//        ReportResponseDTO report = reportService.getReportById(id);
//        return new ApiResponse<>(true, "Report retrieved", report);
//    }
//
//    @GetMapping("/reports/status/{status}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<ReportResponseDTO>> getReportsByStatus(
//            @PathVariable Report.ReportStatus status,
//            Pageable pageable) {
//        Page<ReportResponseDTO> reports = reportService.getReportsByStatus(status, pageable);
//        return new ApiResponse<>(true, "Reports filtered by status", reports);
//    }
//
//    @GetMapping("/reports/type/{type}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<ReportResponseDTO>> getReportsByType(
//            @PathVariable Report.ReportType type,
//            Pageable pageable) {
//        Page<ReportResponseDTO> reports = reportService.getReportsByType(type, pageable);
//        return new ApiResponse<>(true, "Reports filtered by type", reports);
//    }
//
//    @GetMapping("/reports/filter")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<ReportResponseDTO>> getFilteredReports(
//            @RequestParam(required = false) Report.ReportStatus status,
//            @RequestParam(required = false) Report.ReportType type,
//            @RequestParam(required = false) Long adminId,
//            Pageable pageable) {
//        Page<ReportResponseDTO> reports = reportService.getFilteredReports(status, type, adminId, pageable);
//        return new ApiResponse<>(true, "Filtered reports retrieved", reports);
//    }
//
//    @GetMapping("/reports/user/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<List<ReportResponseDTO>> getReportsAgainstUser(@PathVariable Long userId) {
//        List<ReportResponseDTO> reports = reportService.getReportsAgainstUser(userId);
//        return new ApiResponse<>(true, "Reports against user retrieved", reports);
//    }
//
//    @PutMapping("/reports/{id}/status")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<ReportResponseDTO> updateReportStatus(
//            @PathVariable Long id,
//            @RequestParam Report.ReportStatus status) {
//        ReportResponseDTO report = reportService.updateReportStatus(id, status);
//        return new ApiResponse<>(true, "Report status updated", report);
//    }
//
//    @PutMapping("/reports/{id}/assign")
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
//    public ApiResponse<ReportResponseDTO> assignReportToAdmin(
//            @PathVariable Long id,
//            @RequestParam Long adminId) {
//        ReportResponseDTO report = reportService.assignReportToAdmin(id, adminId);
//        return new ApiResponse<>(true, "Report assigned to admin", report);
//    }
//
//    @PutMapping("/reports/{id}/notes")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<ReportResponseDTO> addAdminNotes(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> request) {
//        String notes = request.get("notes");
//        ReportResponseDTO report = reportService.addAdminNotes(id, notes);
//        return new ApiResponse<>(true, "Admin notes added", report);
//    }
//
//    @PutMapping("/reports/{id}/resolve")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<ReportResponseDTO> resolveReport(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> request) {
//        String resolution = request.get("resolution");
//        ReportResponseDTO report = reportService.resolveReport(id, resolution);
//        return new ApiResponse<>(true, "Report resolved", report);
//    }
//
//    @DeleteMapping("/reports/{id}")
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
//    public ApiResponse<String> deleteReport(@PathVariable Long id) {
//        reportService.deleteReport(id);
//        return new ApiResponse<>(true, "Report deleted successfully", null);
//    }
//
//    // ================= REPORT STATISTICS =================
//
//    @GetMapping("/reports/stats")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Map<String, Long>> getReportStats() {
//        Map<String, Long> stats = Map.of(
//                "totalReports", reportService.getReportCountByStatus(Report.ReportStatus.PENDING) +
//                                reportService.getReportCountByStatus(Report.ReportStatus.IN_PROGRESS) +
//                                reportService.getReportCountByStatus(Report.ReportStatus.RESOLVED) +
//                                reportService.getReportCountByStatus(Report.ReportStatus.CLOSED),
//                "pendingReports", reportService.getPendingReportsCount(),
//                "resolvedReports", reportService.getReportCountByStatus(Report.ReportStatus.RESOLVED),
//                "closedReports", reportService.getReportCountByStatus(Report.ReportStatus.CLOSED)
//        );
//        return new ApiResponse<>(true, "Report statistics retrieved", stats);
//    }
//
//    @GetMapping("/reports/distribution")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<List<Object[]>> getReportTypeDistribution() {
//        List<Object[]> distribution = reportService.getReportTypeDistribution();
//        return new ApiResponse<>(true, "Report type distribution retrieved", distribution);
//    }
//
//    // ================= DATE RANGE REPORTS =================
//
//    @GetMapping("/reports/range")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<Page<ReportResponseDTO>> getReportsByDateRange(
//            @RequestParam String startDate,
//            @RequestParam String endDate,
//            Pageable pageable) {
//        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
//        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
//        java.time.LocalDateTime startDateTime = start.atStartOfDay();
//        java.time.LocalDateTime endDateTime = end.atTime(23, 59, 59);
//
//        Page<ReportResponseDTO> reports = reportService.getReportsByDateRange(startDateTime, endDateTime, pageable);
//        return new ApiResponse<>(true, "Reports for date range retrieved", reports);
//    }
//
//    // ================= HEALTH CHECK =================
//
//    @GetMapping("/health")
//    public ResponseEntity<Map<String, Object>> healthCheck() {
//        return ResponseEntity.ok(Map.of(
//                "status", "UP",
//                "service", "Admin Operations API",
//                "timestamp", java.time.LocalDateTime.now()
//        ));
//    }
//}
//
