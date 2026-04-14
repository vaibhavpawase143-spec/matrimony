package com.example.service;

import com.example.model.*;
import com.example.repository.UserBlockRepository;
import com.example.repository.UserReportRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final UserBlockRepository userBlockRepository;

    private static final int BLOCK_THRESHOLD = 5;

    // ✅ REPORT USER
    @Transactional
    public String reportUser(String reporterEmail, Long reportedUserId, String reason) {

        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        if (reporter.getId().equals(reportedUserId)) {
            throw new RuntimeException("You cannot report yourself");
        }

        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Already blocked check
        if (userBlockRepository.existsByBlockedIdAndIsActiveTrue(reportedUserId)) {
            return "User already blocked";
        }

        boolean alreadyReported = userReportRepository
                .existsByReporterAndReportedUser(reporter, reportedUser);

        if (alreadyReported) {
            return "You already reported this user";
        }

        // ✅ Save report
        UserReport report = UserReport.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .status(ReportStatus.PENDING)
                .build();

        userReportRepository.save(report);

        long reportCount = userReportRepository.countByReportedUser(reportedUser);

        // 🔥 AUTO BLOCK
        if (reportCount >= BLOCK_THRESHOLD) {

            boolean alreadyBlocked =
                    userBlockRepository.existsByBlockedIdAndIsActiveTrue(reportedUserId);

            if (!alreadyBlocked) {

                UserBlock block = new UserBlock();
                block.setBlockerId(0L); // SYSTEM
                block.setBlockedId(reportedUserId);

                userBlockRepository.save(block);
            }
        }

        return "User reported successfully";
    }

    // ✅ ADMIN APIs
    public List<UserReport> getAllReports() {
        return userReportRepository.findAll();
    }

    public List<UserReport> getPendingReports() {
        return userReportRepository.findByStatus(ReportStatus.PENDING);
    }

    @Transactional
    public String markAsReviewed(Long reportId) {

        UserReport report = userReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(ReportStatus.REVIEWED);
        userReportRepository.save(report);

        return "Report marked as reviewed";
    }

    // ✅ ADMIN UNBLOCK (soft)
    @Transactional
    public String unblockUser(Long userId) {

        UserBlock block = userBlockRepository
                .findByBlockerIdAndBlockedId(0L, userId)
                .orElse(null);

        if (block == null || !Boolean.TRUE.equals(block.getIsActive())) {
            return "User is not blocked";
        }

        block.setIsActive(false);
        userBlockRepository.save(block);

        return "User unblocked successfully";
    }
}