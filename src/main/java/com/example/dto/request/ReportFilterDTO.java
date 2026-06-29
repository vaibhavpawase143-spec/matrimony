package com.example.dto.request;

import com.example.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterDTO {

    // Global Search
    private String search;

    // Filter by Status
    private ReportStatus status;

    // Filter by Reporter
    private String reporterName;

    // Filter by Reported User
    private String reportedUserName;

    // Date Range
    private LocalDate fromDate;
    private LocalDate toDate;
}