package com.example.service;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.dto.response.UserDetailsResponse;
import org.springframework.data.domain.Page;

public interface UserDetailsSearchService {
    Page<UserDetailsResponse> search(UserDetailsSearchRequest request);
}