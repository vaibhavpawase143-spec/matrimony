package com.example.repository;

import com.example.model.User;
import com.example.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    long countByReportedUser(User user);
    boolean existsByReporterAndReportedUser(User reporter, User reportedUser);
}