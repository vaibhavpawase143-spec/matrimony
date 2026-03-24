package com.example.controller.master;

import com.example.dto.request.StateRequestDTO;
import com.example.dto.response.StateResponseDTO;
import com.example.model.Admin;
import com.example.model.Country;
import com.example.model.State;
import com.example.service.StateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService service;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public StateResponseDTO create(@Valid @RequestBody StateRequestDTO dto) {

        State entity = mapToEntity(dto);
        State saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public StateResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody StateRequestDTO dto) {

        State existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        updateEntity(existing, dto);

        State updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public StateResponseDTO getById(@PathVariable Long id) {

        State s = service.getById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        return mapToResponse(s);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<StateResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<StateResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<StateResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // COUNTRY FILTER
    // =========================

    @GetMapping("/country/{countryId}/admin/{adminId}")
    public List<StateResponseDTO> getByCountryAndAdmin(
            @PathVariable Long countryId,
            @PathVariable Long adminId) {

        return service.getByCountryAndAdmin(countryId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/country/{countryId}/admin/{adminId}/active")
    public List<StateResponseDTO> getActiveByCountryAndAdmin(
            @PathVariable Long countryId,
            @PathVariable Long adminId) {

        return service.getActiveByCountryAndAdmin(countryId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<StateResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "State deleted successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private State mapToEntity(StateRequestDTO dto) {

        State s = new State();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        s.setAdmin(admin);

        Country country = new Country();
        country.setId(dto.getCountryId());
        s.setCountry(country);

        s.setName(dto.getName());

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }

        return s;
    }

    private void updateEntity(State s, StateRequestDTO dto) {

        // 🚫 DO NOT update admin

        if (dto.getCountryId() != null) {
            Country country = new Country();
            country.setId(dto.getCountryId());
            s.setCountry(country);
        }

        if (dto.getName() != null) {
            s.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }
    }

    private StateResponseDTO mapToResponse(State s) {

        return StateResponseDTO.builder()
                .id(s.getId())
                .adminId(s.getAdmin() != null ? s.getAdmin().getId() : null)
                .adminName(s.getAdmin() != null ? s.getAdmin().getName() : null)
                .countryId(s.getCountry() != null ? s.getCountry().getId() : null)
                .countryName(s.getCountry() != null ? s.getCountry().getName() : null)
                .name(s.getName())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}