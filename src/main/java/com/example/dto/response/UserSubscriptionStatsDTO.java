package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionStatsDTO {

    private Long totalSubscriptions;

    private Long activeSubscriptions;

    private Long inactiveSubscriptions;

    private Long activeStatus;

    private Long cancelledStatus;

    private Long expiredStatus;

    private Long refundedStatus;
}