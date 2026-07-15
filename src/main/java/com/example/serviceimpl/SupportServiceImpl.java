package com.example.serviceimpl;

import com.example.dto.request.SupportRequestDTO;
import com.example.dto.response.SupportResponseDTO;
import com.example.model.*;
import com.example.repository.SupportTicketRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.CurrentAdminService;
import com.example.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportTicketRepository repository;
    private final UserRepository userRepository;
    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService adminAuditLogService;
    // ================= USER: CREATE TICKET =================

    @Override
    @Transactional
    public SupportResponseDTO createTicket(
            SupportRequestDTO request
    ) {

        User user = getLoggedInUser();

        SupportTicket ticket = new SupportTicket();

        ticket.setTicketNumber(
                "SUP-"
                        + LocalDate.now().getYear()
                        + "-"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 6)
                        .toUpperCase()
        );

        ticket.setUser(user);
        ticket.setCategory(request.getCategory());
        ticket.setSubject(request.getSubject());
        ticket.setMessage(request.getMessage());
        ticket.setAttachmentUrl(request.getAttachmentUrl());

        ticket.setPriority(SupportPriority.MEDIUM);
        ticket.setStatus(SupportStatus.OPEN);

        repository.save(ticket);

        return mapToDTO(ticket);
    }

    // ================= USER: MY TICKETS =================

    @Override
    @Transactional(readOnly = true)
    public List<SupportResponseDTO> getMyTickets() {

        User user = getLoggedInUser();

        return repository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ================= USER: TICKET DETAILS =================

    @Override
    @Transactional(readOnly = true)
    public SupportResponseDTO getTicketByNumber(
            String ticketNumber
    ) {

        User user = getLoggedInUser();

        SupportTicket ticket = findTicketByNumber(ticketNumber);

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return mapToDTO(ticket);
    }

    // ================= USER: CLOSE TICKET =================

    @Override
    @Transactional
    public void closeTicket(
            String ticketNumber
    ) {

        User user = getLoggedInUser();

        SupportTicket ticket = findTicketByNumber(ticketNumber);

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (ticket.getStatus() == SupportStatus.CLOSED) {
            return;
        }

        ticket.setStatus(SupportStatus.CLOSED);
        ticket.setResolvedAt(LocalDateTime.now());

        repository.save(ticket);
    }

    // ================= ADMIN: ALL TICKETS =================

    @Override
    @Transactional(readOnly = true)
    public List<SupportResponseDTO> getAllTickets() {

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ================= ADMIN: ONE TICKET =================

    @Override
    @Transactional(readOnly = true)
    public SupportResponseDTO getTicketForAdmin(
            String ticketNumber
    ) {

        SupportTicket ticket = findTicketByNumber(ticketNumber);

        return mapToDTO(ticket);
    }

    // ================= ADMIN: UPDATE STATUS =================

    @Override
    @Transactional
    public SupportResponseDTO updateStatus(
            String ticketNumber,
            String status
    ) {

        SupportTicket ticket = findTicketByNumber(ticketNumber);
        SupportStatus oldStatus = ticket.getStatus();
        try {
            ticket.setStatus(
                    SupportStatus.valueOf(
                            status.trim().toUpperCase()
                    )
            );
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("Invalid support ticket status");
        }

        if (
                ticket.getStatus() == SupportStatus.RESOLVED
                        || ticket.getStatus() == SupportStatus.CLOSED
        ) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else {
            ticket.setResolvedAt(null);
        }

        SupportTicket updatedTicket = repository.save(ticket);

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                "SUPPORT_MANAGEMENT",
                "SUPPORT_STATUS_UPDATED",
                "SUPPORT_TICKET",
                updatedTicket.getId(),
                "Updated support ticket status: " + updatedTicket.getTicketNumber(),
                "Status=" + oldStatus,
                "Status=" + updatedTicket.getStatus(),
                "SYSTEM",
                "SYSTEM"
        );

        return mapToDTO(updatedTicket);
    }

    // ================= ADMIN: REPLY =================

    @Override
    @Transactional
    public SupportResponseDTO replyTicket(
            String ticketNumber,
            String reply
    ) {

        SupportTicket ticket = findTicketByNumber(ticketNumber);
        String oldReply = ticket.getAdminReply();
        SupportStatus oldStatus = ticket.getStatus();
        ticket.setAdminReply(reply.trim());

        // First reply automatically moves OPEN ticket to IN_PROGRESS
        if (ticket.getStatus() == SupportStatus.OPEN) {
            ticket.setStatus(SupportStatus.IN_PROGRESS);
        }
        SupportTicket updatedTicket = repository.save(ticket);

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                "SUPPORT_MANAGEMENT",
                "SUPPORT_REPLY_ADDED",
                "SUPPORT_TICKET",
                updatedTicket.getId(),
                "Admin replied to support ticket: " + updatedTicket.getTicketNumber(),
                "Reply=" + (oldReply == null ? "No Reply" : oldReply)
                        + ", Status=" + oldStatus,
                "Reply=" + updatedTicket.getAdminReply()
                        + ", Status=" + updatedTicket.getStatus(),
                "SYSTEM",
                "SYSTEM"
        );
        return mapToDTO(updatedTicket);
    }

    // ================= HELPERS =================

    private SupportTicket findTicketByNumber(
            String ticketNumber
    ) {

        return repository.findByTicketNumber(ticketNumber)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Support ticket not found"
                        )
                );
    }

    private SupportResponseDTO mapToDTO(
            SupportTicket ticket
    ) {

        SupportResponseDTO dto = new SupportResponseDTO();

        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());

        dto.setUserId(ticket.getUser().getId());
        dto.setUserName(ticket.getUser().getFullName());

        dto.setCategory(ticket.getCategory());
        dto.setSubject(ticket.getSubject());
        dto.setMessage(ticket.getMessage());
        dto.setAttachmentUrl(ticket.getAttachmentUrl());

        dto.setPriority(ticket.getPriority());
        dto.setStatus(ticket.getStatus());

        dto.setAdminReply(ticket.getAdminReply());

        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        dto.setResolvedAt(ticket.getResolvedAt());

        return dto;
    }

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

}