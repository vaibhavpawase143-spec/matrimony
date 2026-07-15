package com.example.controller.master;

import com.example.dto.request.CountryRequestDTO;
import com.example.dto.response.CountryResponseDTO;
import com.example.model.Admin;
import com.example.model.Country;
import com.example.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // ================= CREATE =================
    @PostMapping
    public CountryResponseDTO create(@Valid @RequestBody CountryRequestDTO dto) {

        Country saved = countryService.create(mapToEntity(dto));
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public CountryResponseDTO getById(@PathVariable Long id) {

        return countryService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<CountryResponseDTO> getAll() {

        return countryService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<CountryResponseDTO> getActive() {

        return countryService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public List<CountryResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return countryService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<CountryResponseDTO> search(@RequestParam String keyword) {

        return countryService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public CountryResponseDTO update(@PathVariable Long id,
                                     @Valid @RequestBody CountryRequestDTO dto) {

        Country updated = countryService.update(id, mapToEntity(dto));
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        countryService.delete(id);
        return "Country deleted successfully";
    }

    // ================= MAPPERS =================

    private Country mapToEntity(CountryRequestDTO dto) {

        Country country = new Country();

        country.setName(dto.getName());

        // Admin mapping
        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        country.setAdmin(admin);

        country.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return country;
    }

    private CountryResponseDTO mapToResponse(Country country) {

        CountryResponseDTO dto = new CountryResponseDTO();

        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setIsActive(country.getIsActive());
        dto.setCreatedAt(country.getCreatedAt());

        dto.setAdminId(country.getAdminId());

        return dto;
    }
}