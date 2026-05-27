package com.example.dto.response;

import lombok.Data;

@Data
public class ProkeralaTokenResponseDTO {

    private String access_token;
    private String token_type;
    private int expires_in;

    public long getExpiresIn() {
        return 0;
    }


    public String getAccessToken() {
        return null;
    }
}