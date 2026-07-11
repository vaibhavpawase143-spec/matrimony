package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserStatsDTO {

    private Long totalUsers;

    private Long activeUsers;

    private Long inactiveUsers;

    private Long blockedUsers;

    private Long deletedUsers;

    private Long emailVerifiedUsers;

    private Long phoneVerifiedUsers;

}