package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Builder.Default
    private boolean success = false;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String errorCode;
    private String code;
    private String message;
    private String field;
    private Map<String, String> fieldErrors;
    private String path;
}