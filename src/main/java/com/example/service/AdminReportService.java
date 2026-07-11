package com.example.service;

import com.example.dto.request.ReportFilterDTO;
import com.example.dto.response.AdminReportResponseDTO;
import com.example.dto.response.AdminReportStatsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminReportService {

    Page<AdminReportResponseDTO> getAllReports(
            ReportFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );

    AdminReportResponseDTO getReportById(Long id);

    List<AdminReportResponseDTO> getPendingReports();

    AdminReportResponseDTO markAsReviewed(Long id);

    AdminReportResponseDTO approveReport(Long id);

    AdminReportResponseDTO rejectReport(Long id);

    AdminReportStatsDTO getReportStatistics();
}