package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStatsDTO {

    // Total admins
    private Long totalAdmins;

    // Active admins
    private Long activeAdmins;

    // Inactive admins
    private Long inactiveAdmins;

    // Super admins
    private Long superAdmins;

    // Normal admins
    private Long normalAdmins;

    // New admins created this month
    private Long newAdminsThisMonth;
}