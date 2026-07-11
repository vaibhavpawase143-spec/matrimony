package com.example.dto.request;

import lombok.Data;

@Data
public class FieldOfStudyRequestDTO {

    private String name;
    private Boolean isActive;
    private Long adminId;
}