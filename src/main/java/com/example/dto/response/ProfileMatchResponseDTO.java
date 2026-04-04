package com.example.dto.responce;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileMatchResponseDTO {

    private Long userId;
    private String userName;
    private Integer matchPercentage;

}