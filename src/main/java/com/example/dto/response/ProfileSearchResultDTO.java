package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchResultDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private Integer age;

    private Long genderId;
    private String genderName;

    private Long religionId;
    private String religionName;

    private Long casteId;
    private String casteName;

    private Long subCasteId;
    private String subCasteName;

    private Long motherTongueId;
    private String motherTongueName;

    private Long maritalStatusId;
    private String maritalStatusName;

    private Long cityId;
    private String cityName;

    private Long stateId;
    private String stateName;

    private Long countryId;
    private String countryName;

    private Long educationLevelId;
    private String educationLevelName;

    private Long occupationId;
    private String occupationName;

    private Long incomeId;
    private String incomeName;

    private Long heightId;
    private String heightValue;

    private Long weightId;
    private String weightValue;

    private Long dietId;
    private String dietName;

    private Long smokingId;
    private String smokingName;

    private Long drinkingId;
    private String drinkingName;

    private Long manglikStatusId;
    private String manglikStatusName;

    private Long profileTypeId;
    private String profileTypeName;

    private Boolean profileCompleted;
    private Boolean isPremium;
    private Boolean verified;

    private LocalDateTime createdAt;
}
