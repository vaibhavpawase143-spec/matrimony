package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlockedUserResponseDTO {

    private Long blockedUserId;

    private String fullName;

    private String photoUrl;

    private LocalDateTime blockedDate;

}