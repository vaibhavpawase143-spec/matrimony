package com.example.controller.master;

import com.example.dto.request.CountryRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CountryResponseDTO;
import com.example.model.Admin;
import com.example.model.Country;
import com.example.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<CountryResponseDTO> create(
            @Valid @RequestBody CountryRequestDTO dto) {

        Country saved = countryService.create(mapToEntity(dto));

        return ApiResponse.<CountryResponseDTO>builder()
                .success(true)
                .message("Country created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<CountryResponseDTO> getById(
            @PathVariable Long id) {

        Country country = countryService.getById(id);

        return ApiResponse.<CountryResponseDTO>builder()
                .success(true)
                .message("Country retrieved successfully.")
                .data(mapToResponse(country))
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<CountryResponseDTO>> getAll() {

        List<CountryResponseDTO> countries = countryService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CountryResponseDTO>>builder()
                .success(true)
                .message("Countries retrieved successfully.")
                .data(countries)
                .build();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<CountryResponseDTO>> getActive() {

        List<CountryResponseDTO> countries = countryService.getActive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CountryResponseDTO>>builder()
                .success(true)
                .message("Active countries retrieved successfully.")
                .data(countries)
                .build();
    }
    // ================= GET BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<CountryResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        List<CountryResponseDTO> countries = countryService
                .getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CountryResponseDTO>>builder()
                .success(true)
                .message("Countries retrieved successfully by admin.")
                .data(countries)
                .build();
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ApiResponse<List<CountryResponseDTO>> search(
            @RequestParam String keyword) {

        List<CountryResponseDTO> countries = countryService
                .search(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CountryResponseDTO>>builder()
                .success(true)
                .message("Countries searched successfully.")
                .data(countries)
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<CountryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CountryRequestDTO dto) {

        Country updated = countryService.update(
                id,
                mapToEntity(dto)
        );

        return ApiResponse.<CountryResponseDTO>builder()
                .success(true)
                .message("Country updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        countryService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Country deleted successfully.")
                .build();
    }

    // ================= GET DELETED =================
    @GetMapping("/deleted")
    public ApiResponse<List<CountryResponseDTO>> getDeleted() {

        List<CountryResponseDTO> deleted = countryService.getDeleted()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CountryResponseDTO>>builder()
                .success(true)
                .message("Deleted countries retrieved successfully.")
                .data(deleted)
                .build();
    }

    // ================= RESTORE =================
    @PutMapping("/restore/{id}")
    public ApiResponse<CountryResponseDTO> restore(
            @PathVariable Long id) {

        Country restored = countryService.restore(id);

        return ApiResponse.<CountryResponseDTO>builder()
                .success(true)
                .message("Country restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }

    // ================= HARD DELETE =================
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        countryService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Country permanently deleted successfully.")
                .build();
    }
    // ================= MAPPERS =================

    private Country mapToEntity(CountryRequestDTO dto) {

        Country country = new Country();

        country.setName(dto.getName());

        // Admin mapping
        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        country.setAdmin(admin);

        country.setIsActive(
                dto.getIsActive() != null
                        ? dto.getIsActive()
                        : true
        );

        return country;
    }

    private CountryResponseDTO mapToResponse(Country country) {

        CountryResponseDTO dto = new CountryResponseDTO();

        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setIsActive(country.getIsActive());
        dto.setCreatedAt(country.getCreatedAt());

        if (country.getAdmin() != null) {
            dto.setAdminId(country.getAdmin().getId());
        }

        return dto;
    }
}