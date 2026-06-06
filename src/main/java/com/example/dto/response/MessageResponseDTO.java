package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponseDTO {

    private Long id;

    private Long senderId;

    private String content;

    private LocalDateTime createdAt;

}