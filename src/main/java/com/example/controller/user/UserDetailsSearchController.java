package com.example.controller.user;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.model.UserDetails;
import com.example.service.UserDetailsSearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class UserDetailsSearchController {

    private final UserDetailsSearchService service;

    @PostMapping
    public Page<UserDetails> search(@RequestBody UserDetailsSearchRequest request) {
        return service.search(request);
    }
}