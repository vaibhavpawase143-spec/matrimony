package com.example.controller.master;

import com.example.dto.request.CasteRequestDTO;
import com.example.dto.response.CasteResponseDTO;
import com.example.model.Caste;
import com.example.model.Religion;
import com.example.service.CasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins/{adminId}/castes")
@RequiredArgsConstructor
public class CasteController {

    private final CasteService casteService;

    // ================= CREATE =================
    @PostMapping
    public CasteResponseDTO create(@PathVariable Long adminId,
                                   @Valid @RequestBody CasteRequestDTO dto) {

        Caste saved = casteService.create(mapToEntity(dto), adminId);
        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public CasteResponseDTO getById(@PathVariable Long adminId,
                                    @PathVariable Long id) {

        return mapToResponse(casteService.getById(id, adminId));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<CasteResponseDTO> getAll(@PathVariable Long adminId) {

        return casteService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<CasteResponseDTO> getActive(@PathVariable Long adminId) {

        return casteService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET BY RELIGION =================
    @GetMapping("/religion/{religionId}")
    public List<CasteResponseDTO> getByReligion(@PathVariable Long adminId,
                                                @PathVariable Long religionId) {

        return casteService.getByReligion(religionId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE BY RELIGION =================
    @GetMapping("/religion/{religionId}/active")
    public List<CasteResponseDTO> getActiveByReligion(@PathVariable Long adminId,
                                                      @PathVariable Long religionId) {

        return casteService.getActiveByReligion(religionId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public List<CasteResponseDTO> search(@PathVariable Long adminId,
                                         @RequestParam String keyword) {

        return casteService.search(keyword, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public CasteResponseDTO update(@PathVariable Long adminId,
                                   @PathVariable Long id,
                                   @Valid @RequestBody CasteRequestDTO dto) {

        Caste updated = casteService.update(id, mapToEntity(dto), adminId);
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long adminId,
                         @PathVariable Long id) {

        casteService.delete(id, adminId);
        return "Caste deleted successfully";
    }

    // ================= MAPPERS =================

    private Caste mapToEntity(CasteRequestDTO dto) {

        Caste caste = new Caste();

        caste.setName(dto.getName());

        // 👇 Important: set only ID (Service will fetch full entity)
        Religion religion = new Religion();
        religion.setId(dto.getReligionId());
        caste.setReligion(religion);

        caste.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return caste;
    }

    private CasteResponseDTO mapToResponse(Caste caste) {

        CasteResponseDTO dto = new CasteResponseDTO();

        dto.setId(caste.getId());
        dto.setName(caste.getName());
        dto.setIsActive(caste.getIsActive());
        dto.setCreatedAt(caste.getCreatedAt());

        if (caste.getAdmin() != null) {
            dto.setAdminId(caste.getAdmin().getId());
        }

        if (caste.getReligion() != null) {
            dto.setReligionId(caste.getReligion().getId());
            dto.setReligionName(caste.getReligion().getName());
        }

        return dto;
    }
}