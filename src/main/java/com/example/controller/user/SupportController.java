package com.example.controller.user;

import com.example.dto.request.SupportRequestDTO;
import com.example.dto.response.SupportResponseDTO;
import com.example.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SupportController {

    private final SupportService service;

    // ================= CREATE TICKET =================

    @PostMapping
    public ResponseEntity<SupportResponseDTO> createTicket(

            @Valid
            @RequestBody
            SupportRequestDTO request

    ) {

        return ResponseEntity.ok(

                service.createTicket(request)

        );

    }

    // ================= MY TICKETS =================

    @GetMapping("/me")
    public ResponseEntity<List<SupportResponseDTO>> getMyTickets() {

        return ResponseEntity.ok(

                service.getMyTickets()

        );

    }

    // ================= TICKET DETAILS =================

    @GetMapping("/{ticketNumber}")
    public ResponseEntity<SupportResponseDTO> getTicket(

            @PathVariable String ticketNumber

    ) {

        return ResponseEntity.ok(

                service.getTicketByNumber(ticketNumber)

        );

    }

    // ================= CLOSE TICKET =================

    @PutMapping("/{ticketNumber}/close")
    public ResponseEntity<String> closeTicket(

            @PathVariable String ticketNumber

    ) {

        service.closeTicket(Long.valueOf(ticketNumber));

        return ResponseEntity.ok(

                "Support ticket closed successfully."

        );

    }

}