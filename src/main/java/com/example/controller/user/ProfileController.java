package com.example.controller.user;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.PartnerPreference;
import com.example.serviceimpl.ProfileServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final ProfileServiceImpl service;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ProfileResponseDTO> create(
            @Valid @RequestBody ProfileRequestDTO dto
    ) {
        try {
            return ResponseEntity.ok(service.createProfile(dto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Profile already exists")) {
                ProfileResponseDTO response = new ProfileResponseDTO();
                response.setMessage("Profile already exists. You can update your existing profile instead.");
                return ResponseEntity.badRequest().body(response);
            }
            throw e;
        }
    }

    // ================= CHECK PROFILE STATUS =================
    @GetMapping("/status")
    public ResponseEntity<ProfileResponseDTO> checkProfileStatus() {
        try {
            ProfileResponseDTO profile = service.getMyProfile();
            profile.setMessage("Profile exists and is active");
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Profile not found")) {
                ProfileResponseDTO response = new ProfileResponseDTO();
                response.setMessage("Profile not found. Please create your profile to continue.");
                return ResponseEntity.status(404).body(response);
            }
            throw e;
        }
    }

    // ================= GET MY PROFILE =================
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDTO> getMyProfile() {
        try {
            return ResponseEntity.ok(service.getMyProfile());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Profile not found")) {
                // Return a response indicating profile needs to be created
                ProfileResponseDTO response = new ProfileResponseDTO();
                response.setMessage("Profile not found. Please create your profile first.");
                return ResponseEntity.status(404).body(response);
            }
            throw e;
        }
    }

    // ================= UPDATE MY PROFILE =================
    @PutMapping("/me")
    public ResponseEntity<ProfileResponseDTO> updateMyProfile(
            @RequestBody UpdateProfileRequestDTO dto
    ) {
        return ResponseEntity.ok(service.updateMyProfile(dto));
    }
    @GetMapping("/user/{userId}")

    public ResponseEntity<ProfileResponseDTO>

    getByUserId(

            @PathVariable Long userId

    ){

        return ResponseEntity.ok(

                service.getProfileByUserId(
                        userId
                )

        );

    }
    // ================= GET BY ID (FIXED 🔥) =================
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProfileById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<ProfileResponseDTO>> getAllProfiles() {
        List<ProfileResponseDTO> list = service.getAll()
                .stream()
                .map(service::mapToDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================= SEARCH =================
    @PostMapping("/search")
    public ResponseEntity<Page<ProfileResponseDTO>> searchProfiles(
            @RequestBody PartnerPreference pref,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.searchProfiles(pref, pageable));
    }

    // ================= FILTER: CITY =================
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<ProfileResponseDTO>> getByCity(@PathVariable Long cityId) {
        List<ProfileResponseDTO> list = service.getByCity(cityId)
                .stream()
                .map(service::mapToDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    // ================= FILTER: RELIGION =================
    @GetMapping("/religion/{religionId}")
    public ResponseEntity<List<ProfileResponseDTO>> getByReligion(@PathVariable Long religionId) {
        List<ProfileResponseDTO> list = service.getByReligion(religionId)
                .stream()
                .map(service::mapToDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    // ================= FILTER: ACTIVE =================
    @GetMapping("/active")
    public ResponseEntity<List<ProfileResponseDTO>> getActiveProfiles() {
        List<ProfileResponseDTO> list = service.getActiveProfiles()
                .stream()
                .map(service::mapToDTO)
                .toList();

        return ResponseEntity.ok(list);
    }
}