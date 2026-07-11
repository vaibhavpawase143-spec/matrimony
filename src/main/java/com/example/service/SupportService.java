package com.example.service;

import com.example.dto.request.SupportRequestDTO;
import com.example.dto.response.SupportResponseDTO;

import java.util.List;

public interface SupportService {

    // ================= USER =================

    SupportResponseDTO createTicket(
            SupportRequestDTO request
    );

    List<SupportResponseDTO> getMyTickets();

    SupportResponseDTO getTicketByNumber(
            String ticketNumber
    );

    void closeTicket(
            String ticketNumber
    );

    // ================= ADMIN =================

    List<SupportResponseDTO> getAllTickets();

    SupportResponseDTO getTicketForAdmin(
            String ticketNumber
    );

    SupportResponseDTO updateStatus(
            String ticketNumber,
            String status
    );

    SupportResponseDTO replyTicket(
            String ticketNumber,
            String reply
    );

}