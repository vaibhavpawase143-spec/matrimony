package com.example.serviceimpl;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.service.CurrentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentAdminServiceImpl implements CurrentAdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin getCurrentAdmin() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return adminRepository.findByEmailWithRole(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged-in admin not found"));
    }
}