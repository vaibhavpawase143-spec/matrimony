package com.example.dto.request;

import lombok.Data;

@Data
public class UserFilterDTO {

    private String search;      // name/email
    private Boolean isActive;
    private Boolean isDeleted;
    private String role;
    private Boolean isBlocked;

    private Boolean emailVerified;

    private Boolean phoneVerified;// ROLE_USER, ROLE_ADMIN
}