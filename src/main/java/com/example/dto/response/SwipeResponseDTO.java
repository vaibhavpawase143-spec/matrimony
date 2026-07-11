package com.example.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SwipeResponseDTO {

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private String type;

}