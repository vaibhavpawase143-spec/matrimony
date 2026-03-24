package com.example.controller.master;

import com.example.dto.request.BrotherTypeRequestDTO;
import com.example.dto.responce.BrotherTypeResponseDTO;
import com.example.model.BrotherType;
import com.example.service.BrotherTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins/{adminId}/brother-types")
@RequiredArgsConstructor
public class BrotherTypeController {

    private final BrotherTypeService brotherTypeService;

    // ================= CREATE =================
    @PostMapping
    public BrotherTypeResponseDTO create(@PathVariable Long adminId,
                                         @Valid @RequestBody BrotherTypeRequestDTO dto) {

        BrotherType saved = brotherTypeService.create(mapToEntity(dto), adminId);
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public BrotherTypeResponseDTO getById(@PathVariable Long adminId,
                                          @PathVariable Long id) {

        return mapToResponse(brotherTypeService.getById(id, adminId));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<BrotherTypeResponseDTO> getAll(@PathVariable Long adminId) {

        return brotherTypeService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<BrotherTypeResponseDTO> getActive(@PathVariable Long adminId) {

        return brotherTypeService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<BrotherTypeResponseDTO> search(@PathVariable Long adminId,
                                               @RequestParam String keyword) {

        return brotherTypeService.search(keyword, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public BrotherTypeResponseDTO update(@PathVariable Long adminId,
                                         @PathVariable Long id,
                                         @Valid @RequestBody BrotherTypeRequestDTO dto) {

        BrotherType updated = brotherTypeService.update(id, mapToEntity(dto), adminId);
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long adminId,
                         @PathVariable Long id) {

        brotherTypeService.delete(id, adminId);
        return "Brother type deleted successfully";
    }

    // ================= MAPPERS =================

    private BrotherType mapToEntity(BrotherTypeRequestDTO dto) {

        BrotherType entity = new BrotherType();

        entity.setValue(dto.getValue());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return entity;
    }

    private BrotherTypeResponseDTO mapToResponse(BrotherType entity) {

        BrotherTypeResponseDTO dto = new BrotherTypeResponseDTO();

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