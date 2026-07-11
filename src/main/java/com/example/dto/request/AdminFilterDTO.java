package com.example.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminFilterDTO {

    // Search by name, username or email
    private String keyword;

    // Filter by role
    private String role;

    // Filter active/inactive
    private Boolean isActive;

    // Filter by created date
    private LocalDate fromDate;

    private LocalDate toDate;

    // Pagination
    private Integer page = 0;

    private Integer size = 10;

    // Sorting
    private String sortBy = "createdAt";

    private String direction = "desc";
}