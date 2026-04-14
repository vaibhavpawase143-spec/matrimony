package com.example.controller.user;

import com.example.model.UserReport;
import com.example.security.SecurityUtils;
import com.example.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;

    // ✅ REPORT USER
    @PostMapping
    public ResponseEntity<String> reportUser(
            @RequestParam Long reportedUserId,
            @RequestParam(required = false) String reason
    ) {

        // 🔥 GET LOGGED-IN USER FROM JWT
        String reporterEmail = SecurityUtils.getCurrentUsername();

        return ResponseEntity.ok(
                userReportService.reportUser(reporterEmail, reportedUserId, reason)
        );
    }

    // ✅ ADMIN: GET ALL REPORTS
    @GetMapping("/all")
    public ResponseEntity<List<UserReport>> getAllReports() {
        return ResponseEntity.ok(userReportService.getAllReports());
    }

    // ✅ ADMIN: GET PENDING REPORTS
    @GetMapping("/pending")
    public ResponseEntity<List<UserReport>> getPendingReports() {
        return ResponseEntity.ok(userReportService.getPendingReports());
    }

    // ✅ ADMIN: MARK REVIEWED
    @PutMapping("/review/{id}")
    public ResponseEntity<String> markReviewed(@PathVariable Long id) {
        return ResponseEntity.ok(userReportService.markAsReviewed(id));
    }

    // ✅ ADMIN: UNBLOCK USER
    @PutMapping("/unblock/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userReportService.unblockUser(userId));
    }
}