package com.example.controller;

import com.example.dto.response.WeightResponseDTO;
import com.example.model.Weight;
import com.example.service.WeightService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/weights")
// @RequiredArgsConstructor
public class PublicWeightController {

    private final WeightService service;

    public PublicWeightController(WeightService service) {
        this.service = service;
    }

//    @GetMapping
//    public List<WeightResponseDTO> getAll() {
//        return service.getAll()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/{id}")
//    public WeightResponseDTO getById(@PathVariable Long id) {
//        Weight weight = service.getById(id)
//                .orElseThrow(() -> new RuntimeException("Weight not found"));
//        return mapToResponse(weight);
//    }
//
//    private WeightResponseDTO mapToResponse(Weight weight) {
//        return WeightResponseDTO.builder()
//                .id(weight.getId())
//                .adminId(weight.getAdmin() != null ? weight.getAdmin().getId() : null)
//                .adminName(null)
//                .value(weight.getValue())
//                .isActive(weight.getIsActive())
//                .createdAt(weight.getCreatedAt())
//                .updatedAt(weight.getUpdatedAt())
//                .build();
//    }
}