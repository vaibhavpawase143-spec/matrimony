package com.example.controller.master;

import com.example.dto.request.CityRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.CityResponseDTO;
import com.example.model.Admin;
import com.example.model.City;
import com.example.model.State;
import com.example.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<CityResponseDTO> create(
            @Valid @RequestBody CityRequestDTO dto) {

        City saved = cityService.create(mapToEntity(dto));

        return ApiResponse.<CityResponseDTO>builder()
                .success(true)
                .message("City created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<CityResponseDTO> getById(
            @PathVariable Long id) {

        City city = cityService.getById(id);

        return ApiResponse.<CityResponseDTO>builder()
                .success(true)
                .message("City retrieved successfully.")
                .data(mapToResponse(city))
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<CityResponseDTO>> getAll() {

        List<CityResponseDTO> cities = cityService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Cities retrieved successfully.")
                .data(cities)
                .build();
    }

    // ================= DELETE =================
    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        cityService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("City deleted successfully.")
                .build();
    }

    // ================= ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<CityResponseDTO>> getActive() {

        List<CityResponseDTO> activeCities = cityService.getActive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Active cities retrieved successfully.")
                .data(activeCities)
                .build();
    }

    // ================= BY STATE =================
    @GetMapping("/state/{stateId}")
    public ApiResponse<List<CityResponseDTO>> getByState(
            @PathVariable Long stateId) {

        List<CityResponseDTO> cities = cityService.getByState(stateId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Cities retrieved successfully by state.")
                .data(cities)
                .build();
    }

    // ================= BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<CityResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        List<CityResponseDTO> cities = cityService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Cities retrieved successfully by admin.")
                .data(cities)
                .build();
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ApiResponse<List<CityResponseDTO>> search(
            @RequestParam String keyword) {

        List<CityResponseDTO> cities = cityService.searchByName(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Cities searched successfully.")
                .data(cities)
                .build();
    }
    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<CityResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CityRequestDTO dto) {

        City updated = cityService.update(
                id,
                mapToEntity(dto)
        );

        return ApiResponse.<CityResponseDTO>builder()
                .success(true)
                .message("City updated successfully.")
                .data(mapToResponse(updated))
                .build();
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

        if (city.getState() != null) {
            dto.setStateId(city.getState().getId());
            dto.setStateName(city.getState().getName());
        }

        if (city.getAdmin() != null) {
            dto.setAdminId(city.getAdmin().getId());
        }

        return dto;
    }
    // ================= GET DELETED =================
    @GetMapping("/deleted")
    public ApiResponse<List<CityResponseDTO>> getDeleted() {

        List<CityResponseDTO> deleted = cityService.getDeleted()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<CityResponseDTO>>builder()
                .success(true)
                .message("Deleted cities retrieved successfully.")
                .data(deleted)
                .build();
    }
    // ================= RESTORE =================
    @PutMapping("/restore/{id}")
    public ApiResponse<CityResponseDTO> restore(
            @PathVariable Long id) {

        City restored = cityService.restore(id);

        return ApiResponse.<CityResponseDTO>builder()
                .success(true)
                .message("City restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }
    // ================= HARD DELETE =================
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        cityService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("City permanently deleted successfully.")
                .build();
    }

}