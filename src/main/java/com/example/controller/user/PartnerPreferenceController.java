package com.example.controller.user;

import com.example.dto.request.PartnerPreferenceRequestDTO;
import com.example.dto.response.PartnerPreferenceResponseDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.PartnerPreferenceService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/partner-preferences")
public class PartnerPreferenceController {

    private final PartnerPreferenceService preferenceService;
    private final UserRepository userRepository;
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final CityRepository cityRepository;

    public PartnerPreferenceController(
            PartnerPreferenceService preferenceService,
            UserRepository userRepository,
            ReligionRepository religionRepository,
            CasteRepository casteRepository,
            CityRepository cityRepository
    ) {
        this.preferenceService = preferenceService;
        this.userRepository = userRepository;
        this.religionRepository = religionRepository;
        this.casteRepository = casteRepository;
        this.cityRepository = cityRepository;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<PartnerPreferenceResponseDTO> create(
            @Valid @RequestBody PartnerPreferenceRequestDTO dto) {

        // 🔥 FETCH USER
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartnerPreference preference = new PartnerPreference();
        preference.setUser(user);

        preference.setMinAge(dto.getMinAge());
        preference.setMaxAge(dto.getMaxAge());
        preference.setMinHeight(dto.getMinHeight());
        preference.setMaxHeight(dto.getMaxHeight());
        preference.setIsActive(dto.getIsActive());

        // 🔥 RELATIONS
        if (dto.getReligionId() != null) {
            preference.setReligion(
                    religionRepository.findById(dto.getReligionId())
                            .orElseThrow(() -> new RuntimeException("Religion not found"))
            );
        }

        if (dto.getCasteId() != null) {
            preference.setCaste(
                    casteRepository.findById(dto.getCasteId())
                            .orElseThrow(() -> new RuntimeException("Caste not found"))
            );
        }

        if (dto.getCityId() != null) {
            preference.setCity(
                    cityRepository.findById(dto.getCityId())
                            .orElseThrow(() -> new RuntimeException("City not found"))
            );
        }

        PartnerPreference saved = preferenceService.savePreference(preference);

        return ResponseEntity.ok(mapToResponse(saved));
    }

    // ✅ UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<PartnerPreferenceResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody PartnerPreferenceRequestDTO dto) {

        PartnerPreference existing = preferenceService.getByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found"));

        existing.setMinAge(dto.getMinAge());
        existing.setMaxAge(dto.getMaxAge());
        existing.setMinHeight(dto.getMinHeight());
        existing.setMaxHeight(dto.getMaxHeight());
        existing.setIsActive(dto.getIsActive());

        if (dto.getReligionId() != null) {
            existing.setReligion(
                    religionRepository.findById(dto.getReligionId())
                            .orElseThrow(() -> new RuntimeException("Religion not found"))
            );
        }

        if (dto.getCasteId() != null) {
            existing.setCaste(
                    casteRepository.findById(dto.getCasteId())
                            .orElseThrow(() -> new RuntimeException("Caste not found"))
            );
        }

        if (dto.getCityId() != null) {
            existing.setCity(
                    cityRepository.findById(dto.getCityId())
                            .orElseThrow(() -> new RuntimeException("City not found"))
            );
        }

        PartnerPreference updated = preferenceService.savePreference(existing);

        return ResponseEntity.ok(mapToResponse(updated));
    }

    // ✅ GET BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<PartnerPreferenceResponseDTO> getByUserId(@PathVariable Long userId) {

        Optional<PartnerPreference> pp = preferenceService.getByUserId(userId);

        return pp.map(p -> ResponseEntity.ok(mapToResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {

        Optional<PartnerPreference> existing = preferenceService.getByUserId(userId);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        preferenceService.delete(existing.get().getId());

        return ResponseEntity.ok("Deleted successfully");
    }
    // ✅ FILTER APIs
    @GetMapping("/religion/{religionId}")
    public ResponseEntity<List<PartnerPreferenceResponseDTO>> getByReligion(@PathVariable Long religionId) {
        return ResponseEntity.ok(
                preferenceService.getByReligion(religionId)
                        .stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/caste/{casteId}")
    public ResponseEntity<List<PartnerPreferenceResponseDTO>> getByCaste(@PathVariable Long casteId) {
        return ResponseEntity.ok(
                preferenceService.getByCaste(casteId)
                        .stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<PartnerPreferenceResponseDTO>> getByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(
                preferenceService.getByCity(cityId)
                        .stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList())
        );
    }

    // ✅ MAPPER
    private PartnerPreferenceResponseDTO mapToResponse(PartnerPreference p) {

        PartnerPreferenceResponseDTO dto = new PartnerPreferenceResponseDTO();

        dto.setId(p.getId());
        dto.setUserId(p.getUser().getId());

        dto.setMinAge(p.getMinAge());
        dto.setMaxAge(p.getMaxAge());

        dto.setMinHeight(p.getMinHeight());
        dto.setMaxHeight(p.getMaxHeight());

        dto.setIsActive(p.getIsActive());

        if (p.getReligion() != null)
            dto.setReligionId(p.getReligion().getId());

        if (p.getCaste() != null)
            dto.setCasteId(p.getCaste().getId());

        if (p.getCity() != null)
            dto.setCityId(p.getCity().getId());

        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        return dto;
    }
}