package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

    @Entity
    @Table(name = "support_tickets")
    public class SupportTicket {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Ticket Number
        @Column(nullable = false, unique = true)
        private String ticketNumber;

        // User who created ticket
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        // Category
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private SupportCategory category;

        // Priority
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private SupportPriority priority;

        // Status
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private SupportStatus status;

        // Subject
        @Column(nullable = false)
        private String subject;

        // User Message
        @Column(columnDefinition = "TEXT")
        private String message;

        // Screenshot (optional)
        private String attachmentUrl;

        // Admin Reply
        @Column(columnDefinition = "TEXT")
        private String adminReply;

        // Assigned Admin
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_admin_id")
        private Admin assignedAdmin;

        // Resolution Time
        private LocalDateTime resolvedAt;

        // Audit
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        updatedAt = LocalDateTime.now();

        status = SupportStatus.OPEN;

        priority = SupportPriority.MEDIUM;

    }
    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();

    }
    }

