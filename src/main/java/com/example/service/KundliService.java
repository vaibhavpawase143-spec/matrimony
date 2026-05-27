package com.example.service;

import com.example.dto.request.KundliMatchRequestDTO;
import com.example.dto.request.KundliMatchRequestDTO;
import com.example.dto.response.KundliMatchResponseDTO;
import com.example.dto.response.KundliMatchResponseDTO;

public interface KundliService {
    KundliMatchResponseDTO match(KundliMatchRequestDTO request);
}