package com.example.dto.response;

import com.example.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReportResponseDTO {

    private Long id;

    // Reporter Details
    private Long reporterId;
    private String reporterName;
    private String reporterEmail;

    // Reported User Details
    private Long reportedUserId;
    private String reportedUserName;
    private String reportedUserEmail;

    // Report Details
    private String reason;
    private ReportStatus status;

    // Audit
    private LocalDateTime createdAt;
}