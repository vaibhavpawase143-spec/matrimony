package com.example.dto.request;

import lombok.Data;

@Data
public class CallRequestDTO {

    private Long receiverId;

    private String type; // AUDIO / VIDEO

}