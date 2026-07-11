package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReportStatsDTO {

    private Long totalReports;

    private Long pendingReports;

    private Long reviewedReports;

    private Long approvedReports;

    private Long rejectedReports;
}