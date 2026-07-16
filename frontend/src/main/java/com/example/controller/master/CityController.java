package com.example.controller.master;

import com.example.dto.request.CityRequestDTO;
import com.example.dto.responce.CityResponseDTO;
import com.example.model.Admin;
import com.example.model.City;
import com.example.model.State;
import com.example.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    // ================= CREATE =================
    @PostMapping
    public CityResponseDTO create(@Valid @RequestBody CityRequestDTO dto) {

        City saved = cityService.saveCity(mapToEntity(dto));
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public CityResponseDTO getById(@PathVariable Long id) {

        return cityService.getCityById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("City not found"));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<CityResponseDTO> getAll() {

        return cityService.getAllCities()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cityService.deleteCity(id);
        return "City deleted successfully";
    }

    // ================= ACTIVE =================
    @GetMapping("/active")
    public List<CityResponseDTO> getActive() {
        return cityService.getActiveCities()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= BY STATE =================
    @GetMapping("/state/{stateId}")
    public List<CityResponseDTO> getByState(@PathVariable Long stateId) {

        return cityService.getCitiesByState(stateId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public List<CityResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return cityService.getCitiesByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<CityResponseDTO> search(@RequestParam String keyword) {

        return cityService.searchByName(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= MAPPERS =================

    private City mapToEntity(CityRequestDTO dto) {

        City city = new City();

        city.setName(dto.getName());

        // State mapping
        State state = new State();
        state.setId(dto.getStateId());
        city.setState(state);

        // Admin mapping
        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        city.setAdmin(admin);

        city.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return city;
    }

    private CityResponseDTO mapToResponse(City city) {

        CityResponseDTO dto = new CityResponseDTO();

        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setIsActive(city.getIsActive());
        dto.setCreatedAt(city.getCreatedAt());

        dto.setStateId(city.getStateId());
        dto.setAdminId(city.getAdminId());

        if (city.getState() != null) {
            dto.setStateName(city.getState().getName());
        }

        return dto;
    }
}