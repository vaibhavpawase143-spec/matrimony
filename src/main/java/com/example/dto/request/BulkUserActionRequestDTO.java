package com.example.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BulkUserActionRequestDTO {

    @NotEmpty(message = "User IDs cannot be empty")
    private List<Long> userIds;

}