package com.example.dto.response;

import com.example.model.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDTO {
    private Long id;
    private Long reportedById;
    private String reportedByName;
    private String reportedByEmail;
    private Long reportedUserId;
    private String reportedUserName;
    private String reportedUserEmail;
    private String reason;
    private String description;
    private Report.ReportType reportType;
    private Report.ReportStatus status;
    private Long assignedAdminId;
    private String assignedAdminName;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}

