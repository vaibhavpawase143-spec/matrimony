package com.example.service;

import com.example.dto.request.AdminProfileUpdateDTO;
import com.example.dto.request.ChangePasswordDTO;
import com.example.dto.response.AdminProfileResponseDTO;

public interface AdminProfileService {

    /**
     * Get currently logged-in admin profile
     */
    AdminProfileResponseDTO getProfile();

    /**
     * Update current admin profile
     */
    AdminProfileResponseDTO updateProfile(
            AdminProfileUpdateDTO dto
    );

    /**
     * Change current admin password
     */
    void changePassword(
            ChangePasswordDTO dto
    );
}