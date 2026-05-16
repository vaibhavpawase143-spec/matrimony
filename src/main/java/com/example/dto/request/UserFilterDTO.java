package com.example.dto.request;

import lombok.Data;

@Data
public class UserFilterDTO {

    private String search;      // name/email
    private Boolean isActive;
    private Boolean isDeleted;
    private String role;        // ROLE_USER, ROLE_ADMIN
}