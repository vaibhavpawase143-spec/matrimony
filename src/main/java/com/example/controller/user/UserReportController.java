package com.example.controller.user;

import com.example.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;

    @PostMapping
    public String reportUser(
            @RequestParam Long reporterId,
            @RequestParam Long reportedUserId,
            @RequestParam(required = false) String reason
    ) {
        return userReportService.reportUser(reporterId, reportedUserId, reason);
    }
}