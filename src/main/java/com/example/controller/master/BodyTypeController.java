package com.example.controller.master;

import com.example.dto.request.BodyTypeRequestDTO;
import com.example.dto.responce.BodyTypeResponseDTO;
import com.example.model.BodyType;
import com.example.service.BodyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins/{adminId}/body-types")
@RequiredArgsConstructor
public class BodyTypeController {

    private final BodyTypeService bodyTypeService;

    // ================= CREATE =================
    @PostMapping
    public BodyTypeResponseDTO create(@PathVariable Long adminId,
                                      @Valid @RequestBody BodyTypeRequestDTO dto) {

        BodyType saved = bodyTypeService.create(mapToEntity(dto), adminId);
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public BodyTypeResponseDTO getById(@PathVariable Long adminId,
                                       @PathVariable Long id) {

        return mapToResponse(bodyTypeService.getById(id, adminId));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<BodyTypeResponseDTO> getAll(@PathVariable Long adminId) {

        return bodyTypeService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<BodyTypeResponseDTO> getActive(@PathVariable Long adminId) {

        return bodyTypeService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET INACTIVE =================
    @GetMapping("/inactive")
    public List<BodyTypeResponseDTO> getInactive(@PathVariable Long adminId) {

        return bodyTypeService.getInactive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public BodyTypeResponseDTO update(@PathVariable Long adminId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody BodyTypeRequestDTO dto) {

        BodyType updated = bodyTypeService.update(id, mapToEntity(dto), adminId);
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long adminId,
                         @PathVariable Long id) {

        bodyTypeService.delete(id, adminId);
        return "Body type deleted successfully";
    }

    // ================= MAPPERS =================

    private BodyType mapToEntity(BodyTypeRequestDTO dto) {

        BodyType bt = new BodyType();

        bt.setValue(dto.getValue());
        bt.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return bt;
    }

    private BodyTypeResponseDTO mapToResponse(BodyType bt) {

        BodyTypeResponseDTO dto = new BodyTypeResponseDTO();

        dto.setId(bt.getId());
        dto.setValue(bt.getValue());
        dto.setIsActive(bt.getIsActive());
        dto.setCreatedAt(bt.getCreatedAt());

        if (bt.getAdmin() != null) {
            dto.setAdminId(bt.getAdmin().getId());
        }

        return dto;
    }
}