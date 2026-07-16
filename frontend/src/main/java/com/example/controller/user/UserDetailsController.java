package com.example.controller.user;

import com.example.model.UserDetails;
import com.example.service.UserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserDetailsService service;

    @GetMapping("/{id}/details")
    public UserDetails getUserDetails(@PathVariable Long id) {
        return service.getUserDetails(id);
    }
}