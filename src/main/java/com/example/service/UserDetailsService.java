package com.example.service;

import com.example.model.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {

    // =========================
    // 🔐 LOAD USER BY EMAIL
    // =========================
    org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    UserDetails getUserDetails(Long userId);

}