package com.example.dto.request;

import lombok.Data;

@Data
public class HeightRequestDTO {

    private String height;
    private Boolean isActive;
    private Long adminId;
}