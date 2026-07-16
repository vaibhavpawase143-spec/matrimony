package com.example.serviceimpl;

import com.example.dto.request.ReportFilterDTO;
import com.example.dto.response.AdminReportResponseDTO;
import com.example.dto.response.AdminReportStatsDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.AdminReportMapper;
import com.example.model.Admin;
import com.example.model.ReportStatus;
import com.example.model.UserReport;
import com.example.repository.UserReportRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminReportService;
import com.example.service.CurrentAdminService;
import com.example.specification.ReportSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

    private final UserReportRepository userReportRepository;
    private final AdminReportMapper adminReportMapper;
    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService adminAuditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminReportResponseDTO> getAllReports(
            ReportFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserReport> reports = userReportRepository.findAll(
                ReportSpecification.getReports(filter),
                pageable
        );

        return reports.map(adminReportMapper::toDTO);
    }
    @Override
    @Transactional(readOnly = true)
    public AdminReportResponseDTO getReportById(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        return adminReportMapper.toDTO(report);

    }

    @Override
    @Transactional(readOnly =true)
    public List<AdminReportResponseDTO> getPendingReports() {

        List<UserReport> reports =
                userReportRepository.findByStatus(ReportStatus.PENDING);

        return reports.stream()
                .map(adminReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminReportResponseDTO markAsReviewed(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        Admin admin = currentAdminService.getCurrentAdmin();
        ReportStatus oldStatus = report.getStatus();
        report.setStatus(ReportStatus.UNDER_REVIEW);
        report.setReviewedBy(admin);
        report.setReviewedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        UserReport updatedReport = userReportRepository.save(report);

        adminAuditLogService.log(
                admin.getId(),
                "REPORT_MANAGEMENT",
                "REPORT_MARKED_UNDER_REVIEW",
                "USER_REPORT",
                updatedReport.getId(),
                "Marked report " + id + " as under review",
                "Status = " + oldStatus,
                "Status = " + ReportStatus.UNDER_REVIEW
        );

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    public AdminReportResponseDTO approveReport(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
            throw new IllegalArgumentException(
                    "Only reports under review can be approved."
            );
        }

        Admin admin = currentAdminService.getCurrentAdmin();
        ReportStatus oldStatus = report.getStatus();
        report.setStatus(ReportStatus.APPROVED);
        report.setReviewedBy(admin);
        report.setReviewedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        UserReport updatedReport = userReportRepository.save(report);

        adminAuditLogService.log(
                admin.getId(),
                "REPORT_MANAGEMENT",
                "REPORT_APPROVED",
                "USER_REPORT",
                updatedReport.getId(),
                "Approved report " + id,
                "Status = " + oldStatus,
                "Status = " + ReportStatus.APPROVED
        );

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    public AdminReportResponseDTO rejectReport(Long id) {

        UserReport report = userReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found with ID: " + id
                        ));

        if (report.getStatus() != ReportStatus.UNDER_REVIEW) {
            throw new IllegalArgumentException(
                    "Only reports under review can be rejected."
            );
        }

        Admin admin = currentAdminService.getCurrentAdmin();
        ReportStatus oldStatus = report.getStatus();
        report.setStatus(ReportStatus.REJECTED);
        report.setReviewedBy(admin);
        report.setReviewedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        UserReport updatedReport = userReportRepository.save(report);

        adminAuditLogService.log(
                admin.getId(),
                "REPORT_MANAGEMENT",
                "REPORT_REJECTED",
                "USER_REPORT",
                updatedReport.getId(),
                "Rejected report " + id,
                "Status = " + oldStatus,
                "Status = " + ReportStatus.REJECTED
        );

        return adminReportMapper.toDTO(updatedReport);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminReportStatsDTO getReportStatistics() {
        return AdminReportStatsDTO.builder()
                .totalReports(userReportRepository.count())
                .pendingReports(userReportRepository.countByStatus(ReportStatus.PENDING))
                .reviewedReports(userReportRepository.countByStatus(ReportStatus.UNDER_REVIEW))
                .approvedReports(userReportRepository.countByStatus(ReportStatus.APPROVED))
                .rejectedReports(userReportRepository.countByStatus(ReportStatus.REJECTED))
                .build();
    }
}