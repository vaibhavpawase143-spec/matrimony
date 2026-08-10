package com.example.service;

import com.example.dto.response.DashboardSummaryDTO;

public interface DashboardService {

    DashboardSummaryDTO getSummary(Long userId);

}