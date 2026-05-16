package com.example.service;

import com.example.dto.response.ReportResponseDTO;
import com.example.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    // ================= REPORT MANAGEMENT =================
    ReportResponseDTO createReport(Long reportedUserId, String reason, String description, String reportType);

    ReportResponseDTO getReportById(Long reportId);

    Page<ReportResponseDTO> getAllReports(Pageable pageable);

    Page<ReportResponseDTO> getReportsByStatus(Report.ReportStatus status, Pageable pageable);

    Page<ReportResponseDTO> getReportsByType(Report.ReportType reportType, Pageable pageable);

    Page<ReportResponseDTO> getFilteredReports(Report.ReportStatus status, Report.ReportType type, Long adminId, Pageable pageable);

    ReportResponseDTO updateReportStatus(Long reportId, Report.ReportStatus status);

    ReportResponseDTO assignReportToAdmin(Long reportId, Long adminId);

    ReportResponseDTO addAdminNotes(Long reportId, String notes);

    ReportResponseDTO resolveReport(Long reportId, String resolution);

    void deleteReport(Long reportId);

    List<ReportResponseDTO> getReportsAgainstUser(Long userId);

    // ================= STATISTICS =================
    Long getReportCountByStatus(Report.ReportStatus status);

    Long getPendingReportsCount();

    List<Object[]> getReportTypeDistribution();

    Page<ReportResponseDTO> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    boolean isDuplicateReport(Long reportedUserId, Long reportedById);
}

