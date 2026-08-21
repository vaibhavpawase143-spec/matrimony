package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sender (User/Admin)
    private Long senderId;

    // Receiver (User)
    private Long receiverId;
    private Long matchedUserId;

    private Integer matchPercentage;

    // NEW FIELD
    @Column(length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    private boolean read = false;

    private boolean deleted = false;
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "event_type", length = 50)
    private String eventType;

    private LocalDateTime createdAt = LocalDateTime.now();
}