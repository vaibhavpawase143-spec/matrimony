package com.example.service;

import com.example.model.User;
import com.example.model.UserReport;
import com.example.repository.UserReportRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    @Transactional
    public String reportUser(Long reporterId, Long reportedUserId, String reason) {

        if (reporterId.equals(reportedUserId)) {
            throw new RuntimeException("You cannot report yourself");
        }

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ FIXED
        if (Boolean.TRUE.equals(reportedUser.getIsBlocked())) {
            return "User already blocked";
        }

        boolean alreadyReported = userReportRepository
                .existsByReporterAndReportedUser(reporter, reportedUser);

        if (alreadyReported) {
            return "You already reported this user";
        }

        UserReport report = UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        userReportRepository.save(report);

        long reportCount = userReportRepository.countByReportedUser(reportedUser);

        reportedUser.setReportCount((int) reportCount);

        // 🚨 AUTO BLOCK
        if (reportCount >= 5) {
            reportedUser.setIsBlocked(true); // ✅ FIXED
        }

        userRepository.save(reportedUser);

        return "User reported successfully";
    }
}