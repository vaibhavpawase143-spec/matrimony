package com.example.dto.request;

import lombok.Data;

@Data
public class SendMessageRequestDTO {

    private Long receiverId;

    private String content;

    private String mediaUrl;

    private String mediaType;

    private Long replyToMessageId;

}