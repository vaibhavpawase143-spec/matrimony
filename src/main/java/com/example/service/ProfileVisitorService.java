package com.example.service;

import com.example.dto.response.ProfileVisitorResponseDTO;

import java.util.List;

public interface ProfileVisitorService {

    void saveVisit(Long visitedUserId);

    List<ProfileVisitorResponseDTO> getMyVisitors();

}