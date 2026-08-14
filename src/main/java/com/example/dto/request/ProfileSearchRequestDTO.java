package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileSearchRequestDTO {
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    private String search;

    private Integer ageFrom;
    private Integer ageTo;

    private String gender;
    private Long genderId;

    private Long religionId;
    private Long casteId;
    private Long subCasteId;
    private Long motherTongueId;
    private Long maritalStatusId;

    private Long educationId;
    private Long educationLevelId;

    private Long occupationId;
    private Long incomeId;

    private Long heightId;
    private Long minHeightId;
    private Long maxHeightId;

    private Long weightId;
    private Double minWeight;
    private Double maxWeight;

    private Long stateId;
    private Long cityId;
    private Long countryId;

    private Long dietId;
    private Long smokingId;
    private Long drinkingId;

    private Long manglikStatusId;
    private Long profileTypeId;

    @Builder.Default
    private String sortBy = "relevance";

    @Builder.Default
    private String sortOrder = "desc";
}
