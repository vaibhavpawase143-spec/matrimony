package com.example.controller.master;

import com.example.dto.request.SubCasteRequestDTO;
import com.example.dto.response.SubCasteResponseDTO;
import com.example.model.Admin;
import com.example.model.Caste;
import com.example.model.SubCaste;
import com.example.repository.AdminRepository;
import com.example.repository.CasteRepository;
import com.example.service.SubCasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/sub-castes")
@RequiredArgsConstructor
public class SubCasteController {

    private final SubCasteService service;
    private final AdminRepository adminRepository;
    private final CasteRepository casteRepository;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public SubCasteResponseDTO create(@Valid @RequestBody SubCasteRequestDTO dto) {

        SubCaste entity = mapToEntity(dto);
        SubCaste saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public SubCasteResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SubCasteRequestDTO dto) {

        SubCaste existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SubCaste not found"));

        updateEntity(existing, dto);

        SubCaste updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public SubCasteResponseDTO getById(@PathVariable Long id) {

        SubCaste s = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SubCaste not found"));

        return mapToResponse(s);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<SubCasteResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================
    @GetMapping("/admin/{adminId}")
    public List<SubCasteResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<SubCasteResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // CASTE FILTER
    // =========================
    @GetMapping("/caste/{casteId}/admin/{adminId}")
    public List<SubCasteResponseDTO> getByCasteAndAdmin(
            @PathVariable Long casteId,
            @PathVariable Long adminId) {

        return service.getByCasteAndAdmin(casteId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/caste/{casteId}/admin/{adminId}/active")
    public List<SubCasteResponseDTO> getActiveByCasteAndAdmin(
            @PathVariable Long casteId,
            @PathVariable Long adminId) {

        return service.getActiveByCasteAndAdmin(casteId, adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<SubCasteResponseDTO> search(
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
        return "SubCaste deleted successfully";
    }

    // =========================
    // 🔥 MAPPING (FIXED)
    // =========================

    private SubCaste mapToEntity(SubCasteRequestDTO dto) {

        SubCaste s = new SubCaste();

        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Caste caste = casteRepository.findById(dto.getCasteId())
                .orElseThrow(() -> new RuntimeException("Caste not found"));

        s.setAdmin(admin);
        s.setCaste(caste);
        s.setName(dto.getName());

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }

        return s;
    }

    private void updateEntity(SubCaste s, SubCasteRequestDTO dto) {

        if (dto.getCasteId() != null) {
            Caste caste = casteRepository.findById(dto.getCasteId())
                    .orElseThrow(() -> new RuntimeException("Caste not found"));
            s.setCaste(caste);
        }

        if (dto.getName() != null) {
            s.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }
    }

    private SubCasteResponseDTO mapToResponse(SubCaste s) {

        return SubCasteResponseDTO.builder()
                .id(s.getId())

                // ✅ SAFE (ONLY ID)
                .adminId(s.getAdmin() != null ? s.getAdmin().getId() : null)
                .adminName(null)

                .casteId(s.getCaste() != null ? s.getCaste().getId() : null)
                .casteName(null)

                .name(s.getName())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}