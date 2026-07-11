package com.example.dto.response;

import lombok.Data;

@Data
public class ProfileVisitorResponseDTO {

    private Long userId;

    private Long profileId;

    private String fullName;

    private String email;

    private String imageUrl;

    private String viewedAt;

}