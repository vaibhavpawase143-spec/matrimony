package com.example.mapper;

import com.example.dto.response.AdminReportResponseDTO;
import com.example.model.UserReport;
import org.springframework.stereotype.Component;

@Component
public class AdminReportMapper {

    public AdminReportResponseDTO toDTO(UserReport report) {

        if (report == null) {
            return null;
        }

        AdminReportResponseDTO dto = new AdminReportResponseDTO();

        dto.setId(report.getId());

        // Reporter
        if (report.getReporter() != null) {
            dto.setReporterId(report.getReporter().getId());
            dto.setReporterName(report.getReporter().getFullName());
            dto.setReporterEmail(report.getReporter().getEmail());
        }

        // Reported User
        if (report.getReportedUser() != null) {
            dto.setReportedUserId(report.getReportedUser().getId());
            dto.setReportedUserName(report.getReportedUser().getFullName());
            dto.setReportedUserEmail(report.getReportedUser().getEmail());
        }

        dto.setReason(report.getReason());
        dto.setStatus(report.getStatus());
        dto.setCreatedAt(report.getCreatedAt());

        return dto;
    }
}