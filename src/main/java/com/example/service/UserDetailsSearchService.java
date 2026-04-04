package com.example.service;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.model.UserDetails;
import org.springframework.data.domain.Page;

public interface UserDetailsSearchService {
    Page<UserDetails> search(UserDetailsSearchRequest request);
}