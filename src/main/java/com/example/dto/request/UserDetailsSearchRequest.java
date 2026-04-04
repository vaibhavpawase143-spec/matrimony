package com.example.dto.request;

import lombok.Data;

@Data
public class UserDetailsSearchRequest {

    private String religion;
    private String caste;
    private String city;
    private String education;

    private Integer page = 0;
    private Integer size = 10;

    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}