package com.example.repository;

import com.example.model.SupportStatus;
import com.example.model.SupportTicket;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportTicketRepository
        extends JpaRepository<SupportTicket, Long> {

    // Find all tickets of a user
    List<SupportTicket> findByUser(User user);

    // Find by User ID
    List<SupportTicket> findByUserId(Long userId);

    // Find by Status
    List<SupportTicket> findByStatus(SupportStatus status);

    // Find by Ticket Number
    Optional<SupportTicket> findByTicketNumber(String ticketNumber);

    // Dashboard Counts
    long countByStatus(SupportStatus status);

}