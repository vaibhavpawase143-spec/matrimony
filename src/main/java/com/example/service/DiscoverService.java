package com.example.service;

import com.example.dto.response.DiscoverProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscoverService {

    Page<DiscoverProfileDTO> discoverProfiles(
            Long userId,
            Pageable pageable
    );

}