package com.example.dto.response;

import com.example.model.SupportCategory;
import com.example.model.SupportPriority;
import com.example.model.SupportStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupportResponseDTO {

    private Long id;

    private String ticketNumber;

    private Long userId;

    private String userName;

    private SupportCategory category;

    private String subject;

    private String message;

    private String attachmentUrl;

    private SupportPriority priority;

    private SupportStatus status;

    private String adminReply;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

}