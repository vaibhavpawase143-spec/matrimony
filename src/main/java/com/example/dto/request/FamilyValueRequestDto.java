package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyValueRequestDto {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotBlank(message = "Name is required")
    private String name;

    private Boolean isActive = true;
}