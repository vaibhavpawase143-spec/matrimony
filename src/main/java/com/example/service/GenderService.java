package com.example.service;

import com.example.dto.response.GenderResponseDTO;

import java.util.List;

public interface GenderService {

    List<GenderResponseDTO> getAllGenders();

}