package com.example.service;

import com.example.dto.request.SupportRequestDTO;
import com.example.dto.response.SupportResponseDTO;

import java.util.List;

public interface SupportService {

    // User
    SupportResponseDTO createTicket(
            SupportRequestDTO request
    );

    List<SupportResponseDTO> getMyTickets();

    SupportResponseDTO getTicketByNumber(
            String ticketNumber
    );

    void closeTicket(
            Long ticketNumber
    );

    // Admin (used later)
    List<SupportResponseDTO> getAllTickets();

    void updateStatus(
            Long ticketId,
            String status
    );

    void replyTicket(
            Long ticketId,
            String reply
    );

}