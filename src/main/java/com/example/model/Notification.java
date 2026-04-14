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

    private Long senderId;
    private Long receiverId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean read = false;
    private boolean deleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}