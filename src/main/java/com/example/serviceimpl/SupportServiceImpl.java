package com.example.serviceimpl;

import com.example.dto.request.SupportRequestDTO;
import com.example.dto.response.SupportResponseDTO;
import com.example.model.SupportPriority;
import com.example.model.SupportStatus;
import com.example.model.SupportTicket;
import com.example.model.User;
import com.example.repository.SupportTicketRepository;
import com.example.repository.UserRepository;
import com.example.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportTicketRepository repository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SupportResponseDTO createTicket(
            SupportRequestDTO request
    ) {

        User user =
                getLoggedInUser();

        SupportTicket ticket =
                new SupportTicket();

        ticket.setTicketNumber(

                "SUP-"

                        + LocalDate.now().getYear()

                        + "-"

                        + UUID.randomUUID()
                        .toString()
                        .substring(0,6)
                        .toUpperCase()

        );

        ticket.setUser(user);

        ticket.setCategory(
                request.getCategory()
        );

        ticket.setSubject(
                request.getSubject()
        );

        ticket.setMessage(
                request.getMessage()
        );

        ticket.setAttachmentUrl(
                request.getAttachmentUrl()
        );

        ticket.setPriority(
                SupportPriority.MEDIUM
        );

        ticket.setStatus(
                SupportStatus.OPEN
        );

        repository.save(ticket);

        return mapToDTO(ticket);

    }
    private SupportResponseDTO mapToDTO(
            SupportTicket ticket
    ) {

        SupportResponseDTO dto =
                new SupportResponseDTO();

        dto.setId(
                ticket.getId()
        );

        dto.setTicketNumber(
                ticket.getTicketNumber()
        );

        dto.setUserId(
                ticket.getUser().getId()
        );

        dto.setUserName(
                ticket.getUser().getFullName()
        );

        dto.setCategory(
                ticket.getCategory()
        );

        dto.setSubject(
                ticket.getSubject()
        );

        dto.setMessage(
                ticket.getMessage()
        );

        dto.setAttachmentUrl(
                ticket.getAttachmentUrl()
        );

        dto.setPriority(
                ticket.getPriority()
        );

        dto.setStatus(
                ticket.getStatus()
        );

        dto.setAdminReply(
                ticket.getAdminReply()
        );

        dto.setCreatedAt(
                ticket.getCreatedAt()
        );

        dto.setUpdatedAt(
                ticket.getUpdatedAt()
        );

        dto.setResolvedAt(
                ticket.getResolvedAt()
        );

        return dto;

    }
    @Override
    @Transactional(readOnly = true)
    public List<SupportResponseDTO> getMyTickets() {

        User user =
                getLoggedInUser();

        return repository
                .findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public SupportResponseDTO getTicketByNumber(
            String ticketNumber
    ) {

        User user =
                getLoggedInUser();

        SupportTicket ticket =
                repository
                        .findByTicketNumber(ticketNumber)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Support ticket not found"
                                        )
                        );

        // User can only view his own ticket
        if (
                !ticket.getUser()
                        .getId()
                        .equals(user.getId())
        ) {

            throw new RuntimeException(
                    "Access denied"
            );

        }

        return mapToDTO(ticket);

    }

    @Override
    @Transactional
    public void closeTicket(
            Long ticketId
    ) {

        User user =
                getLoggedInUser();

        SupportTicket ticket =
                repository
                        .findById(ticketId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Support ticket not found"
                                )
                        );

        // User can close only his own ticket
        if (
                !ticket.getUser()
                        .getId()
                        .equals(user.getId())
        ) {

            throw new RuntimeException(
                    "Access denied"
            );

        }

        ticket.setStatus(
                SupportStatus.CLOSED
        );

        ticket.setResolvedAt(
                java.time.LocalDateTime.now()
        );

        repository.save(ticket);

    }

    @Override
    public List<SupportResponseDTO> getAllTickets() {
        return List.of();
    }

    @Override
    public void updateStatus(Long ticketId, String status) {

    }

    @Override
    public void replyTicket(Long ticketId, String reply) {

    }
    private User getLoggedInUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "User not found"
                                )
                );
    }
}