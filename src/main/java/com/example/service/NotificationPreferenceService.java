package com.example.service;

import com.example.dto.request.NotificationPreferenceRequestDTO;
import com.example.dto.response.NotificationPreferenceResponseDTO;

public interface NotificationPreferenceService {

    NotificationPreferenceResponseDTO getMyPreferences();

    NotificationPreferenceResponseDTO updatePreferences(
            NotificationPreferenceRequestDTO request
    );

}