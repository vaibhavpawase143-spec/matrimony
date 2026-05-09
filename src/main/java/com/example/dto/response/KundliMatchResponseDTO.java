package com.example.dto.response;

import lombok.Data;

@Data
public class KundliMatchResponseDTO {
    private int gunaMilanScore;
    private boolean isCompatible;
    private String message;
}