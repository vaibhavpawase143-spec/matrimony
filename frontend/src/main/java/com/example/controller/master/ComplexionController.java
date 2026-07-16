package com.example.controller.master;

import com.example.dto.request.ComplexionRequestDTO;
import com.example.dto.response.ComplexionResponseDTO;
import com.example.model.Admin;
import com.example.model.Complexion;
import com.example.service.ComplexionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/complexions")
@RequiredArgsConstructor
public class ComplexionController {

    private final ComplexionService complexionService;

    // ================= CREATE =================
    @PostMapping
    public ComplexionResponseDTO create(@Valid @RequestBody ComplexionRequestDTO dto) {

        Complexion saved = complexionService.create(mapToEntity(dto));
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ComplexionResponseDTO getById(@PathVariable Long id) {

        return mapToResponse(complexionService.getById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<ComplexionResponseDTO> getAll() {

        return complexionService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<ComplexionResponseDTO> getActive() {

        return complexionService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public List<ComplexionResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return complexionService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<ComplexionResponseDTO> search(@RequestParam String keyword) {

        return complexionService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ComplexionResponseDTO update(@PathVariable Long id,
                                        @Valid @RequestBody ComplexionRequestDTO dto) {

        Complexion updated = complexionService.update(id, mapToEntity(dto));
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        complexionService.delete(id);
        return "Complexion deleted successfully";
    }

    // ================= MAPPERS =================

    private Complexion mapToEntity(ComplexionRequestDTO dto) {

        Complexion entity = new Complexion();

        entity.setValue(dto.getValue());

        // Admin mapping
        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        entity.setAdmin(admin);

        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return entity;
    }

    private ComplexionResponseDTO mapToResponse(Complexion entity) {

        ComplexionResponseDTO dto = new ComplexionResponseDTO();

        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}