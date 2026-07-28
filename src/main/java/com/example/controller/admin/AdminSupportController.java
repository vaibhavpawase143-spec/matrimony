package com.example.controller.admin;

import com.example.dto.request.SupportReplyRequestDTO;
import com.example.dto.request.SupportStatusUpdateRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SupportResponseDTO;
import com.example.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/support")
@RequiredArgsConstructor
public class AdminSupportController {

    private final SupportService service;

    // =====================================================
    // GET ALL SUPPORT TICKETS
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAuthority('SUPPORT_VIEW')")
    public ApiResponse<List<SupportResponseDTO>> getAllTickets() {

        return ApiResponse.<List<SupportResponseDTO>>builder()
                .success(true)
                .message("Support tickets retrieved successfully")
                .data(service.getAllTickets())
                .build();
    }

    // =====================================================
    // GET SUPPORT TICKET BY TICKET NUMBER
    // =====================================================

    @GetMapping("/{ticketNumber}")
    @PreAuthorize("hasAuthority('SUPPORT_VIEW')")
    public ApiResponse<SupportResponseDTO> getTicket(
            @PathVariable String ticketNumber) {

        return ApiResponse.<SupportResponseDTO>builder()
                .success(true)
                .message("Support ticket retrieved successfully")
                .data(service.getTicketForAdmin(ticketNumber))
                .build();
    }

    // =====================================================
    // UPDATE SUPPORT TICKET STATUS
    // =====================================================

    @PutMapping("/{ticketNumber}/status")
    @PreAuthorize("hasAuthority('SUPPORT_CLOSE')")
    public ApiResponse<SupportResponseDTO> updateStatus(
            @PathVariable String ticketNumber,
            @Valid @RequestBody SupportStatusUpdateRequestDTO request) {

        return ApiResponse.<SupportResponseDTO>builder()
                .success(true)
                .message("Support ticket status updated successfully")
                .data(service.updateStatus(
                        ticketNumber,
                        request.getStatus()))
                .build();
    }

    // =====================================================
    // REPLY TO SUPPORT TICKET
    // =====================================================

    @PutMapping("/{ticketNumber}/reply")
    @PreAuthorize("hasAuthority('SUPPORT_REPLY')")
    public ApiResponse<SupportResponseDTO> replyTicket(
            @PathVariable String ticketNumber,
            @Valid @RequestBody SupportReplyRequestDTO request) {

        return ApiResponse.<SupportResponseDTO>builder()
                .success(true)
                .message("Reply sent successfully")
                .data(service.replyTicket(
                        ticketNumber,
                        request.getReply()))
                .build();
    }

}