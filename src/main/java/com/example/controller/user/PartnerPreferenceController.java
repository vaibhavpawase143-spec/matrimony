package com.example.controller.user;

import com.example.dto.request.PartnerPreferenceRequestDTO;
import com.example.dto.response.PartnerPreferenceResponseDTO;
import com.example.model.PartnerPreference;
import com.example.model.User;
import com.example.repository.*;
import com.example.security.SecurityUtils;
import com.example.service.PartnerPreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/partner-preferences")
public class PartnerPreferenceController {

    private final PartnerPreferenceService preferenceService;
    private final UserRepository userRepository;
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final CityRepository cityRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final OccupationRepository occupationRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final SmokingRepository smokingRepository;
    private final DrinkingRepository drinkingRepository;
    private final DietRepository dietRepository;

    public PartnerPreferenceController(
            PartnerPreferenceService preferenceService,
            UserRepository userRepository,
            ReligionRepository religionRepository,
            CasteRepository casteRepository,
            CityRepository cityRepository,
            EducationLevelRepository educationLevelRepository,
            OccupationRepository occupationRepository,
            MaritalStatusRepository maritalStatusRepository,
            SmokingRepository smokingRepository,
            DrinkingRepository drinkingRepository,
            DietRepository dietRepository
    ) {
        this.preferenceService = preferenceService;
        this.userRepository = userRepository;
        this.religionRepository = religionRepository;
        this.casteRepository = casteRepository;
        this.cityRepository = cityRepository;
        this.educationLevelRepository = educationLevelRepository;
        this.occupationRepository = occupationRepository;
        this.maritalStatusRepository = maritalStatusRepository;
        this.smokingRepository = smokingRepository;
        this.drinkingRepository = drinkingRepository;
        this.dietRepository = dietRepository;
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + email));
    }

    private boolean isUserAuthorized(User currentUser, Long targetUserId) {
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));
        return isAdmin || (currentUser.getId() != null && currentUser.getId().equals(targetUserId));
    }

    @PostMapping
    public ResponseEntity<PartnerPreferenceResponseDTO> create(
            @Valid @RequestBody PartnerPreferenceRequestDTO dto
    ){
        User currentUser = getAuthenticatedUser();
        Long targetUserId = (dto.getUserId() != null) ? dto.getUserId() : currentUser.getId();

        if (!isUserAuthorized(currentUser, targetUserId)) {
            throw new AccessDeniedException("Access denied: You cannot create partner preferences for another user");
        }

        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartnerPreference preference = preferenceService.getByUserId(targetUserId)
                .orElseGet(PartnerPreference::new);

        preference.setUser(user);
        applyFields(preference, dto);
        preference.setOtherExpectations(dto.getOtherExpectations());

        PartnerPreference saved = preferenceService.savePreference(preference);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<PartnerPreferenceResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody PartnerPreferenceRequestDTO dto
    ){
        User currentUser = getAuthenticatedUser();
        if (!isUserAuthorized(currentUser, userId)) {
            throw new AccessDeniedException("Access denied: You cannot update partner preferences for another user");
        }

        PartnerPreference preference = preferenceService
                .getByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    PartnerPreference newPref = new PartnerPreference();
                    newPref.setUser(user);
                    return newPref;
                });

        applyFields(
                preference,
                dto
        );

        preference.setOtherExpectations(
                dto.getOtherExpectations()
        );

        PartnerPreference updated = preferenceService.savePreference(
                preference
        );

        return ResponseEntity.ok(
                mapToResponse(updated)
        );
    }

    private void applyFields(
            PartnerPreference preference,
            PartnerPreferenceRequestDTO dto
    ) {
        preference.setMinAge(dto.getMinAge());
        preference.setMaxAge(dto.getMaxAge());
        preference.setMinHeight(dto.getMinHeight());
        preference.setMaxHeight(dto.getMaxHeight());
        preference.setMinWeight(dto.getMinWeight());
        preference.setMaxWeight(dto.getMaxWeight());
        preference.setIsActive(dto.getIsActive());

        if (dto.getReligionId() != null) {
            preference.setReligion(
                    religionRepository.findById(dto.getReligionId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Religion not found for ID: " + dto.getReligionId()
                            ))
            );
        } else {
            preference.setReligion(null);
        }

        if (dto.getCasteId() != null) {
            preference.setCaste(
                    casteRepository.findById(dto.getCasteId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Caste not found for ID: " + dto.getCasteId()
                            ))
            );
        } else {
            preference.setCaste(null);
        }

        if (dto.getCityId() != null) {
            preference.setCity(
                    cityRepository.findById(dto.getCityId())
                            .orElseThrow(() -> new RuntimeException(
                                    "City not found for ID: " + dto.getCityId()
                            ))
            );
        } else {
            preference.setCity(null);
        }

        if (dto.getEducationLevelId() != null) {
            preference.setEducationLevel(
                    educationLevelRepository.findById(dto.getEducationLevelId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Education level not found for ID: " + dto.getEducationLevelId()
                            ))
            );
        } else {
            preference.setEducationLevel(null);
        }

        if (dto.getOccupationId() != null) {
            preference.setOccupation(
                    occupationRepository.findById(dto.getOccupationId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Occupation not found for ID: " + dto.getOccupationId()
                            ))
            );
        } else {
            preference.setOccupation(null);
        }

        if (dto.getMaritalStatusId() != null) {
            preference.setMaritalStatus(
                    maritalStatusRepository.findById(dto.getMaritalStatusId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Marital status not found for ID: " + dto.getMaritalStatusId()
                            ))
            );
        } else {
            preference.setMaritalStatus(null);
        }

        if (dto.getSmokingId() != null) {
            preference.setSmoking(
                    smokingRepository.findById(dto.getSmokingId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Smoking status not found for ID: " + dto.getSmokingId()
                            ))
            );
        } else {
            preference.setSmoking(null);
        }

        if (dto.getDrinkingId() != null) {
            preference.setDrinking(
                    drinkingRepository.findById(dto.getDrinkingId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Drinking status not found for ID: " + dto.getDrinkingId()
                            ))
            );
        } else {
            preference.setDrinking(null);
        }

        if (dto.getDietId() != null) {
            preference.setDiet(
                    dietRepository.findById(dto.getDietId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Diet not found for ID: " + dto.getDietId()
                            ))
            );
        } else {
            preference.setDiet(null);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<PartnerPreferenceResponseDTO> getByUserId(
            @PathVariable Long userId
    ){

        Optional<PartnerPreference> pp=
                preferenceService.getByUserId(
                        userId
                );

        return pp.map(
                p->ResponseEntity.ok(
                        mapToResponse(p)
                )
        ).orElse(
                ResponseEntity.notFound().build()
        );

    }


    private PartnerPreferenceResponseDTO mapToResponse(
            PartnerPreference p
    ){

        PartnerPreferenceResponseDTO dto=
                new PartnerPreferenceResponseDTO();

        dto.setId(
                p.getId()
        );

        dto.setUserId(
                p.getUser().getId()
        );

        dto.setMinAge(
                p.getMinAge()
        );

        dto.setMaxAge(
                p.getMaxAge()
        );

        dto.setMinHeight(
                p.getMinHeight()
        );

        dto.setMaxHeight(
                p.getMaxHeight()
        );

// ADD THESE

        dto.setMinWeight(
                p.getMinWeight()
        );

        dto.setMaxWeight(
                p.getMaxWeight()
        );
        dto.setIsActive(
                p.getIsActive()
        );

        if(p.getReligion()!=null)
            dto.setReligionId(
                    p.getReligion().getId()
            );

        if(p.getCaste()!=null)
            dto.setCasteId(
                    p.getCaste().getId()
            );

        if(p.getCity()!=null)
            dto.setCityId(
                    p.getCity().getId()
            );

        if(p.getEducationLevel()!=null)
            dto.setEducationLevelId(
                    p.getEducationLevel().getId()
            );

        if(p.getOccupation()!=null)
            dto.setOccupationId(
                    p.getOccupation().getId()
            );

        if(p.getMaritalStatus()!=null)
            dto.setMaritalStatusId(
                    p.getMaritalStatus().getId()
            );

        if(p.getSmoking()!=null)
            dto.setSmokingId(
                    p.getSmoking().getId()
            );

        if(p.getDrinking()!=null)
            dto.setDrinkingId(
                    p.getDrinking().getId()
            );

        if(p.getDiet()!=null)
            dto.setDietId(
                    p.getDiet().getId()
            );
        dto.setOtherExpectations(
                p.getOtherExpectations()
        );
        dto.setCreatedAt(
                p.getCreatedAt()
        );

        dto.setUpdatedAt(
                p.getUpdatedAt()
        );

        return dto;

    }

}