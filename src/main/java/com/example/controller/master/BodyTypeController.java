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
@RequestMapping("/api/body-types")
@RequiredArgsConstructor
public class BodyTypeController {

    private final BodyTypeService bodyTypeService;

    // ================= CREATE =================
    @PostMapping("/{adminId}")
    public BodyTypeResponseDTO create(
            @PathVariable Long adminId,
            @Valid @RequestBody BodyTypeRequestDTO dto
    ) {

        BodyType saved = bodyTypeService.create(mapToEntity(dto), adminId);

        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}/{adminId}")
    public BodyTypeResponseDTO getById(
            @PathVariable Long id,
            @PathVariable Long adminId
    ) {

        return mapToResponse(bodyTypeService.getById(id, adminId));
    }

    // ================= GET ALL (PUBLIC) =================
    @GetMapping
    public List<BodyTypeResponseDTO> getAll() {

        return bodyTypeService.getAll(null)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE (PUBLIC) =================
    @GetMapping("/active")
    public List<BodyTypeResponseDTO> getActive() {

        return bodyTypeService.getActive(null)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET INACTIVE =================
    @GetMapping("/inactive")
    public List<BodyTypeResponseDTO> getInactive() {

        return bodyTypeService.getInactive(null)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}/{adminId}")
    public BodyTypeResponseDTO update(
            @PathVariable Long id,
            @PathVariable Long adminId,
            @Valid @RequestBody BodyTypeRequestDTO dto
    ) {

        BodyType updated =
                bodyTypeService.update(id, mapToEntity(dto), adminId);

        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}/{adminId}")
    public String delete(
            @PathVariable Long id,
            @PathVariable Long adminId
    ) {

        bodyTypeService.delete(id, adminId);

        return "Body type deleted successfully";
    }

    // ================= MAPPERS =================

    private BodyType mapToEntity(BodyTypeRequestDTO dto) {

        BodyType bt = new BodyType();

        bt.setName(dto.getName());

        bt.setIsActive(
                dto.getIsActive() != null
                        ? dto.getIsActive()
                        : true
        );

        return bt;
    }

    private BodyTypeResponseDTO mapToResponse(BodyType bt) {

        BodyTypeResponseDTO dto = new BodyTypeResponseDTO();

        dto.setId(bt.getId());
        dto.setName(bt.getName());
        dto.setIsActive(bt.getIsActive());
        dto.setCreatedAt(bt.getCreatedAt());

        if (bt.getAdmin() != null) {
            dto.setAdminId(bt.getAdmin().getId());
        }

        return dto;
    }
}