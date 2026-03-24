package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterestRequestDTO {

    @NotNull(message = "Sender ID is required")
    private Long senderId;

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
}