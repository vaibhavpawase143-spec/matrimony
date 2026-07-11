package com.example.controller.admin;

import com.example.dto.request.SupportReplyRequestDTO;
import com.example.dto.request.SupportStatusUpdateRequestDTO;
import com.example.dto.response.SupportResponseDTO;
import com.example.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/support")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminSupportController {

    private final SupportService service;

    // ================= ALL SUPPORT TICKETS =================

    @GetMapping
    public ResponseEntity<List<SupportResponseDTO>> getAllTickets() {

        return ResponseEntity.ok(
                service.getAllTickets()
        );
    }

    // ================= ONE TICKET DETAILS =================

    @GetMapping("/{ticketNumber}")
    public ResponseEntity<SupportResponseDTO> getTicket(
            @PathVariable String ticketNumber
    ) {

        return ResponseEntity.ok(
                service.getTicketForAdmin(ticketNumber)
        );
    }

    // ================= UPDATE STATUS =================

    @PutMapping("/{ticketNumber}/status")
    public ResponseEntity<SupportResponseDTO> updateStatus(
            @PathVariable String ticketNumber,
            @Valid @RequestBody SupportStatusUpdateRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.updateStatus(
                        ticketNumber,
                        request.getStatus()
                )
        );
    }

    // ================= ADMIN REPLY =================

    @PutMapping("/{ticketNumber}/reply")
    public ResponseEntity<SupportResponseDTO> replyTicket(
            @PathVariable String ticketNumber,
            @Valid @RequestBody SupportReplyRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.replyTicket(
                        ticketNumber,
                        request.getReply()
                )
        );
    }

}