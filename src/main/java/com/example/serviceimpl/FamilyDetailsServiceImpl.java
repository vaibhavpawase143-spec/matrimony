package com.example.serviceimpl;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.dto.response.FamilyDetailsResponseDto;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.FamilyDetailsService;
import com.example.util.AuditHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyDetailsServiceImpl implements FamilyDetailsService {

    private static final String MODULE = "FAMILY_DETAILS";
    private static final String ENTITY = "FAMILY_DETAILS";

    private final FamilyDetailsRepository familyDetailsRepository;
    private final ProfileRepository profileRepository;
    private final FamilyTypeRepository familyTypeRepository;
    private final FamilyRepository familyRepository;
    private final BrotherTypeRepository brotherTypeRepository;
    private final SisterTypeRepository sisterTypeRepository;

    private final AuditHelper auditHelper;

    // =========================
    // CREATE
    // =========================

    @Override
    public FamilyDetailsResponseDto create(FamilyDetailsRequestDto requestDto) {

        if (familyDetailsRepository.existsByProfile_IdAndDeletedAtIsNull(requestDto.getProfileId())) {
            throw new BadRequestException("Family details already exist for this profile.");
        }

        Profile profile = profileRepository.findById(requestDto.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

        FamilyType familyType = requestDto.getFamilyTypeId() == null ? null :
                familyTypeRepository.findById(requestDto.getFamilyTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Family Type not found."));

        Family family = requestDto.getFamilyId() == null ? null :
                familyRepository.findById(requestDto.getFamilyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Family not found."));

        BrotherType brotherType = requestDto.getBrotherTypeId() == null ? null :
                brotherTypeRepository.findById(requestDto.getBrotherTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Brother Type not found."));

        SisterType sisterType = requestDto.getSisterTypeId() == null ? null :
                sisterTypeRepository.findById(requestDto.getSisterTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sister Type not found."));

        FamilyDetails familyDetails = FamilyDetails.builder()
                .profile(profile)
                .familyType(familyType)
                .family(family)
                .brotherType(brotherType)
                .sisterType(sisterType)
                .fatherOccupation(requestDto.getFatherOccupation())
                .motherOccupation(requestDto.getMotherOccupation())
                .isActive(requestDto.getActive())
                .build();

        FamilyDetails saved = familyDetailsRepository.save(familyDetails);

        auditHelper.logCreate(
                MODULE,
                ENTITY,
                saved.getId(),
                "Profile : " + profile.getId(),
                "Family Details Created"
        );

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public FamilyDetailsResponseDto update(Long id,
                                           FamilyDetailsRequestDto requestDto) {

        FamilyDetails familyDetails = familyDetailsRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Details not found."));

        boolean oldStatus = familyDetails.getIsActive();

        if (requestDto.getFamilyTypeId() != null) {
            familyDetails.setFamilyType(
                    familyTypeRepository.findById(requestDto.getFamilyTypeId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Family Type not found."))
            );
        }

        if (requestDto.getFamilyId() != null) {
            familyDetails.setFamily(
                    familyRepository.findById(requestDto.getFamilyId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Family not found."))
            );
        }

        if (requestDto.getBrotherTypeId() != null) {
            familyDetails.setBrotherType(
                    brotherTypeRepository.findById(requestDto.getBrotherTypeId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Brother Type not found."))
            );
        }

        if (requestDto.getSisterTypeId() != null) {
            familyDetails.setSisterType(
                    sisterTypeRepository.findById(requestDto.getSisterTypeId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Sister Type not found."))
            );
        }

        familyDetails.setFatherOccupation(requestDto.getFatherOccupation());
        familyDetails.setMotherOccupation(requestDto.getMotherOccupation());
        familyDetails.setIsActive(requestDto.getActive());

        FamilyDetails updated = familyDetailsRepository.save(familyDetails);

        auditHelper.logUpdate(
                MODULE,
                ENTITY,
                updated.getId(),
                "Profile : " + updated.getProfile().getId(),
                "Family Details",
                "Family Details Updated",
                oldStatus,
                updated.getIsActive()
        );

        return mapToResponse(updated);
    }

    // =========================
    // SOFT DELETE
    // =========================

    @Override
    public void softDelete(Long id) {

        FamilyDetails familyDetails = familyDetailsRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Details not found."));

        familyDetails.setDeletedAt(LocalDateTime.now());

        familyDetailsRepository.save(familyDetails);

        auditHelper.logDelete(
                MODULE,
                ENTITY,
                familyDetails.getId(),
                "Profile : " + familyDetails.getProfile().getId(),
                "Family Details"
        );
    }
    // =========================
    // RESTORE
    // =========================

    @Override
    public void restore(Long id) {

        FamilyDetails familyDetails = familyDetailsRepository
                .findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Deleted Family Details not found."));

        if (familyDetailsRepository.existsByProfile_IdAndDeletedAtIsNull(
                familyDetails.getProfile().getId())) {

            throw new BadRequestException(
                    "Family Details already exist for this profile.");
        }

        familyDetails.setDeletedAt(null);
        familyDetails.setDeletedBy(null);

        familyDetailsRepository.save(familyDetails);

        auditHelper.logRestore(
                MODULE,
                ENTITY,
                familyDetails.getId(),
                "Profile : " + familyDetails.getProfile().getId(),
                "Family Details Restored"
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @Override
    public void hardDelete(Long id) {

        FamilyDetails familyDetails = familyDetailsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Family Details not found."));

        auditHelper.logHardDelete(
                MODULE,
                ENTITY,
                familyDetails.getId(),
                "Profile : " + familyDetails.getProfile().getId(),
                "Family Details"
        );

        familyDetailsRepository.delete(familyDetails);
    }

    // =========================
    // GET
    // =========================

    @Override
    public FamilyDetailsResponseDto getById(Long id) {

        return mapToResponse(
                familyDetailsRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family Details not found."))
        );
    }

    @Override
    public List<FamilyDetailsResponseDto> getAll() {

        return familyDetailsRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getDeleted() {

        return familyDetailsRepository.findByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FamilyDetailsResponseDto getByProfile(Long profileId) {

        return mapToResponse(
                familyDetailsRepository.findByProfile_IdAndDeletedAtIsNull(profileId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Family Details not found."))
        );
    }

    @Override
    public boolean existsByProfile(Long profileId) {
        return familyDetailsRepository.existsByProfile_IdAndDeletedAtIsNull(profileId);
    }

    @Override
    public List<FamilyDetailsResponseDto> getByFamilyType(Long familyTypeId) {

        return familyDetailsRepository
                .findByFamilyType_IdAndDeletedAtIsNull(familyTypeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getByFamily(Long familyId) {

        return familyDetailsRepository
                .findByFamily_IdAndDeletedAtIsNull(familyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getActiveByFamily(Long familyId) {

        return familyDetailsRepository
                .findByFamily_IdAndFamily_IsActiveTrueAndDeletedAtIsNull(familyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getByBrotherType(Long brotherTypeId) {

        return familyDetailsRepository
                .findByBrotherType_IdAndDeletedAtIsNull(brotherTypeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getBySisterType(Long sisterTypeId) {

        return familyDetailsRepository
                .findBySisterType_IdAndDeletedAtIsNull(sisterTypeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FamilyDetailsResponseDto> getActiveByProfile(Long profileId) {

        return familyDetailsRepository
                .findByProfile_IdAndIsActiveTrueAndDeletedAtIsNull(profileId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // DTO MAPPING
    // =========================

    private FamilyDetailsResponseDto mapToResponse(FamilyDetails entity) {

        return FamilyDetailsResponseDto.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() != null ? entity.getProfile().getId() : null)
                .familyTypeId(entity.getFamilyType() != null ? entity.getFamilyType().getId() : null)
                .familyId(entity.getFamily() != null ? entity.getFamily().getId() : null)
                .brotherTypeId(entity.getBrotherType() != null ? entity.getBrotherType().getId() : null)
                .sisterTypeId(entity.getSisterType() != null ? entity.getSisterType().getId() : null)
                .fatherOccupation(entity.getFatherOccupation())
                .motherOccupation(entity.getMotherOccupation())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }
}