package com.example.controller.admin;

import com.example.dto.response.ApiResponse;
import com.example.model.SubscriptionExpiryJob;
import com.example.model.SubscriptionExpiryJobStatus;
import com.example.repository.SubscriptionExpiryJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subscriptions/expiry-jobs")
@RequiredArgsConstructor
public class AdminSubscriptionExpiryJobController {

    private final SubscriptionExpiryJobRepository jobRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<SubscriptionExpiryJob>> getExpiryJobs(
            @RequestParam(required = false) SubscriptionExpiryJobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SubscriptionExpiryJob> result;

        if (status != null) {
            result = jobRepository.findByStatus(status, pageRequest);
        } else {
            result = jobRepository.findAllWithDetails(pageRequest);
        }

        return ApiResponse.<Page<SubscriptionExpiryJob>>builder()
                .success(true)
                .message("Subscription expiry jobs retrieved successfully")
                .data(result)
                .build();
    }
}
