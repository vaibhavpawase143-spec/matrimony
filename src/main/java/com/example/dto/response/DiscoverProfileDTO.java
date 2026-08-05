package com.example.dto.response;

import lombok.Data;

@Data
public class DiscoverProfileDTO {

    // =====================================================
    // IDS
    // =====================================================

    private Long profileId;
    private Long userId;

    // =====================================================
    // BASIC
    // =====================================================

    private String firstName;
    private String lastName;

    private String imageUrl;

    private Integer age;

    // =====================================================
    // LOCATION
    // =====================================================

    private Long cityId;
    private String cityName;

    // =====================================================
    // RELIGION
    // =====================================================

    private Long religionId;
    private String religionName;

    // =====================================================
    // OCCUPATION
    // =====================================================

    private Long occupationId;
    private String occupationName;

    // =====================================================
    // HEIGHT
    // =====================================================

    private Long heightId;
    private String heightValue;

    // =====================================================
    // PREMIUM
    // =====================================================

    private Boolean premium;

    // =====================================================
    // VERIFIED
    // =====================================================

    private Boolean verified;

    // =====================================================
    // PROFILE
    // =====================================================

    private Integer profileCompletion;
    // =====================================================
// MATCH
// =====================================================

    private Integer matchScore;

    private String matchPercentage;

}