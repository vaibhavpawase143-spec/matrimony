package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionFilterDTO {
    private String search;
    private Boolean isActive;
    private String status;
    private Long planId;
    private LocalDate fromDate;
    private LocalDate toDate;
}
