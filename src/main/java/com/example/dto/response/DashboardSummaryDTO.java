package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {

    private long totalMatches;

    private long interestsSent;

    private long interestsReceived;

    private long shortlists;

    private long profileViews;

    private long likesReceived;

    private long messages;
}