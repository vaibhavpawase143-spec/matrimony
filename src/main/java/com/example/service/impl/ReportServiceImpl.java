package com.example.service.impl;

import com.example.dto.response.ReportResponseDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Report;
import com.example.model.User;
import com.example.repository.ReportRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public ReportResponseDTO createReport(Long reportedUserId, String reason, String description, String reportType) {
        // Get current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User reportedBy = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get reported user
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));

        // Check for duplicate report
        if (isDuplicateReport(reportedUserId, reportedBy.getId())) {
            throw new RuntimeException("Report already exists for this user");
        }

        Report report = new Report();
        report.setReportedBy(reportedBy);
        report.setReportedUser(reportedUser);
        report.setReason(reason);
        report.setDescription(description);
        report.setReportType(Report.ReportType.valueOf(reportType));
        report.setStatus(Report.ReportStatus.PENDING);

        Report saved = reportRepository.save(report);
        return mapToDTO(saved);
    }

    @Override
    public ReportResponseDTO getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return mapToDTO(report);
    }

    @Override
    public Page<ReportResponseDTO> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<ReportResponseDTO> getReportsByStatus(Report.ReportStatus status, Pageable pageable) {
        return reportRepository.findByStatus(status, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<ReportResponseDTO> getReportsByType(Report.ReportType reportType, Pageable pageable) {
        return reportRepository.findByReportType(reportType, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<ReportResponseDTO> getFilteredReports(Report.ReportStatus status, Report.ReportType type, Long adminId, Pageable pageable) {
        return reportRepository.findFiltered(status, type, adminId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public ReportResponseDTO updateReportStatus(Long reportId, Report.ReportStatus status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setStatus(status);
        if (status == Report.ReportStatus.RESOLVED || status == Report.ReportStatus.CLOSED) {
            report.setResolvedAt(LocalDateTime.now());
        }

        Report updated = reportRepository.save(report);
        return mapToDTO(updated);
    }

    @Override
    public ReportResponseDTO assignReportToAdmin(Long reportId, Long adminId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        // Assuming admin exists, we'll just set the ID
        // In production, you'd validate the admin exists
        com.example.model.Admin admin = new com.example.model.Admin();
        admin.setId(adminId);
        report.setAssignedAdmin(admin);
        report.setStatus(Report.ReportStatus.IN_PROGRESS);

        Report updated = reportRepository.save(report);
        return mapToDTO(updated);
    }

    @Override
    public ReportResponseDTO addAdminNotes(Long reportId, String notes) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setAdminNotes(notes);
        Report updated = reportRepository.save(report);
        return mapToDTO(updated);
    }

    @Override
    public ReportResponseDTO resolveReport(Long reportId, String resolution) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setStatus(Report.ReportStatus.RESOLVED);
        report.setAdminNotes(resolution);
        report.setResolvedAt(LocalDateTime.now());

        Report updated = reportRepository.save(report);
        return mapToDTO(updated);
    }

    @Override
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        reportRepository.delete(report);
    }

    @Override
    public List<ReportResponseDTO> getReportsAgainstUser(Long userId) {
        return reportRepository.findReportsAgainstUser(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long getReportCountByStatus(Report.ReportStatus status) {
        return reportRepository.countByStatus(status);
    }

    @Override
    public Long getPendingReportsCount() {
        return reportRepository.countByStatus(Report.ReportStatus.PENDING);
    }

    @Override
    public List<Object[]> getReportTypeDistribution() {
        return reportRepository.countByReportType();
    }

    @Override
    public Page<ReportResponseDTO> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return reportRepository.findByDateRange(startDate, endDate, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public boolean isDuplicateReport(Long reportedUserId, Long reportedById) {
        return reportRepository.existsByReportedUserIdAndReportedById(reportedUserId, reportedById);
    }

    // ================= HELPER =================
    private ReportResponseDTO mapToDTO(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reportedById(report.getReportedBy().getId())
                .reportedByName(report.getReportedBy().getFirstName() + " " + report.getReportedBy().getLastName())
                .reportedByEmail(report.getReportedBy().getEmail())
                .reportedUserId(report.getReportedUser().getId())
                .reportedUserName(report.getReportedUser().getFirstName() + " " + report.getReportedUser().getLastName())
                .reportedUserEmail(report.getReportedUser().getEmail())
                .reason(report.getReason())
                .description(report.getDescription())
                .reportType(report.getReportType())
                .status(report.getStatus())
                .assignedAdminId(report.getAssignedAdmin() != null ? report.getAssignedAdmin().getId() : null)
                .assignedAdminName(report.getAssignedAdmin() != null ? report.getAssignedAdmin().getName() : null)
                .adminNotes(report.getAdminNotes())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .resolvedAt(report.getResolvedAt())
                .build();
    }
}

