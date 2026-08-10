package com.example.repository;

import com.example.dto.response.AdminDashboardDTO;

public interface AdminDashboardQueryRepository {

    AdminDashboardDTO getDashboardStatistics();

}