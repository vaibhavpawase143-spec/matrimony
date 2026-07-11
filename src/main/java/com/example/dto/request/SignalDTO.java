package com.example.dto.request;

import lombok.Data;

@Data
public class SignalDTO {

    private Long targetUserId;

    private String type;

    private Object offer;

    private Object answer;

    private Object candidate;

}