package com.example.serviceimpl;

import com.example.dto.response.GenderResponseDTO;
import com.example.model.Gender;
import com.example.repository.GenderRepository;
import com.example.service.GenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Override
    public List<GenderResponseDTO> getAllGenders() {

        List<Gender> genders = genderRepository.findByIsActiveTrue();

        return genders.stream()
                .map(gender -> new GenderResponseDTO(
                        gender.getId(),
                        gender.getName()
                ))
                .toList();
    }
}