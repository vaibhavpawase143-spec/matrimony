package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDTO {

    @NotNull(message = "Reported user ID is required")
    private Long reportedUserId;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    @NotNull(message = "Report type is required")
    private String reportType;
}

