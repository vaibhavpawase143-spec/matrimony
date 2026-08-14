package com.example.service;

import com.example.dto.request.ProfileSearchRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.ProfileSearchResultDTO;

public interface ProfileSearchService {
    PageResponse<ProfileSearchResultDTO> searchProfiles(ProfileSearchRequestDTO request);
}
