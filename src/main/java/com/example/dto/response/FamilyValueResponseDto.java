package com.example.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyValueResponseDto {

    private Long id;
    private Long adminId;
    private String name;
    private Boolean isActive;
}