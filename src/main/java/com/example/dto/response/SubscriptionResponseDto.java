package com.example.dto.response;

import java.time.LocalDateTime;

public class SubscriptionResponseDto {

    private Long subscriptionId;
    private Long userId;
    private String planName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    public SubscriptionResponseDto() {}

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPlanName() {
        return planName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}