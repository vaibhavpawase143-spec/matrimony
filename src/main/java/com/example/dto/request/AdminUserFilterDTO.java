package com.example.dto.request;

import lombok.Data;

@Data
public class AdminUserFilterDTO {

    private Boolean active;

    private Boolean blocked;

    private Boolean emailVerified;

    private Boolean phoneVerified;

    private String gender;

    private String search;

}