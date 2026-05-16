package com.example.dto.request;

import lombok.Data;

@Data
public class UserDetailsSearchRequest {

    // ===== BASIC FILTERS =====
    private String religion;
    private String caste;
    private String city;
    private String education;

    // 🔥 ADVANCED FILTERS (NOTE: your DB does NOT have real age)
    // 👉 keep these only if you later add DOB
    private Integer minAge;
    private Integer maxAge;

    private Double minHeight;
    private Double maxHeight;

    private String preferredCity;
    private String preferredReligion;

    private Boolean onlyPremium;   // subscription filter
    private Boolean onlyActive;    // active users

    // 🔥 SEARCH KEYWORD
    private String keyword; // name, city, occupation search

    // ===== PAGINATION =====
    private Integer page = 0;
    private Integer size = 10;

    // ===== SORTING =====
    private String sortBy = "createdAt";
    private String sortDirection = "desc";

    // 🔥 VERY IMPORTANT (FOR MATCH SORT)
    private Long userId;
}