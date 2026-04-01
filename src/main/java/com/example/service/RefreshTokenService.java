package com.example.service;

import com.example.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createToken(String email);

    RefreshToken verifyToken(String token);

    void deleteByEmail(String email);
}