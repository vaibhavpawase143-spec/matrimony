package com.example.controller.admin;

import com.example.dto.request.PaymentFilterDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.PaymentResponseDTO;
import com.example.dto.response.PaymentStatsDTO;
import com.example.service.AdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final AdminPaymentService service;

    // ==========================================
    // GET ALL PAYMENTS
    // ==========================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<PaymentResponseDTO>> getAllPayments(

            @RequestParam(required = false) String search,

            @RequestParam(required = false) String status,

            @RequestParam(required = false) String paymentMethod,

            @RequestParam(required = false) Long planId,

            @RequestParam(required = false) BigDecimal minAmount,

            @RequestParam(required = false) BigDecimal maxAmount,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction
    ) {

        PaymentFilterDTO filter = PaymentFilterDTO.builder()
                .search(search)
                .status(status)
                .paymentMethod(paymentMethod)
                .planId(planId)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        return ApiResponse.<Page<PaymentResponseDTO>>builder()
                .success(true)
                .message("Payments retrieved successfully")
                .data(service.getAllPayments(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                ))
                .build();
    }

    // ==========================================
    // GET PAYMENT BY ID
    // ==========================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PaymentResponseDTO> getPaymentById(
            @PathVariable Long id) {

        return ApiResponse.<PaymentResponseDTO>builder()
                .success(true)
                .message("Payment retrieved successfully")
                .data(service.getPaymentById(id))
                .build();
    }

    // ==========================================
    // REFUND PAYMENT
    // ==========================================

    @PutMapping("/{id}/refund")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PaymentResponseDTO> refundPayment(

            @PathVariable Long id,

            @RequestParam BigDecimal refundAmount,

            @RequestParam String refundReason
    ) {

        return ApiResponse.<PaymentResponseDTO>builder()
                .success(true)
                .message("Payment refunded successfully")
                .data(service.refundPayment(
                        id,
                        refundAmount,
                        refundReason
                ))
                .build();
    }

    // ==========================================
    // MARK SUCCESS
    // ==========================================

    @PutMapping("/{id}/mark-success")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PaymentResponseDTO> markPaymentSuccess(
            @PathVariable Long id) {

        return ApiResponse.<PaymentResponseDTO>builder()
                .success(true)
                .message("Payment marked as SUCCESS")
                .data(service.markPaymentSuccess(id))
                .build();
    }

    // ==========================================
    // MARK FAILED
    // ==========================================

    @PutMapping("/{id}/mark-failed")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PaymentResponseDTO> markPaymentFailed(

            @PathVariable Long id,

            @RequestParam String reason
    ) {

        return ApiResponse.<PaymentResponseDTO>builder()
                .success(true)
                .message("Payment marked as FAILED")
                .data(service.markPaymentFailed(id, reason))
                .build();
    }

    // ==========================================
    // PAYMENT STATISTICS
    // ==========================================

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PaymentStatsDTO> getStatistics() {

        return ApiResponse.<PaymentStatsDTO>builder()
                .success(true)
                .message("Payment statistics retrieved successfully")
                .data(service.getStatistics())
                .build();
    }
}