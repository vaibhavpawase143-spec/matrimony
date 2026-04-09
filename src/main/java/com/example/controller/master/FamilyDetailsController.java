package com.example.controller.master;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.dto.response.FamilyDetailsResponseDto;
import com.example.model.FamilyDetails;
import com.example.service.FamilyDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/family-details")
@RequiredArgsConstructor
public class FamilyDetailsController {

    private final FamilyDetailsService service;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<FamilyDetailsResponseDto> create(
            @RequestBody FamilyDetailsRequestDto dto) {

        FamilyDetails entity = service.create(dto);
        return ResponseEntity.ok(mapToDto(entity));
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<FamilyDetailsResponseDto> getById(@PathVariable Long id) {

        FamilyDetails entity = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        return ResponseEntity.ok(mapToDto(entity));
    }

    // ================= GET ALL (ADDED) =================
    @GetMapping
    public ResponseEntity<List<FamilyDetailsResponseDto>> getAll() {

        List<FamilyDetailsResponseDto> list = service.getAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // ================= GET BY PROFILE (IMPORTANT) =================
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<FamilyDetailsResponseDto> getByProfile(@PathVariable Long profileId) {

        FamilyDetails entity = service.getByProfile(profileId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        return ResponseEntity.ok(mapToDto(entity));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<FamilyDetailsResponseDto> update(
            @PathVariable Long id,
            @RequestBody FamilyDetailsRequestDto dto) {

        FamilyDetails entity = service.update(id, dto);
        return ResponseEntity.ok(mapToDto(entity));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ================= DTO MAPPING =================
    private FamilyDetailsResponseDto mapToDto(FamilyDetails e) {
        return FamilyDetailsResponseDto.builder()
                .id(e.getId())
                .profileId(e.getProfile() != null ? e.getProfile().getId() : null)
                .familyTypeId(e.getFamilyType() != null ? e.getFamilyType().getId() : null)
                .familyId(e.getFamily() != null ? e.getFamily().getId() : null)
                .brotherTypeId(e.getBrotherType() != null ? e.getBrotherType().getId() : null)
                .sisterTypeId(e.getSisterType() != null ? e.getSisterType().getId() : null)
                .fatherOccupation(e.getFatherOccupation())
                .motherOccupation(e.getMotherOccupation())
                .build();
    }
}