package com.example.service;

import com.example.model.Profile;
import com.example.repository.ProfileRepository;
import com.example.service.base.BaseAuditService;
import com.example.util.AuditContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Example implementation of ProfileService with audit trail support.
 * 
 * This service demonstrates best practices for:
 * - Using BaseAuditService for common audit operations
 * - Creating records with audit trail
 * - Updating with audit tracking
 * - Soft deleting with reasons
 * - Querying excluding soft-deleted records
 * - Restoring deleted records
 */
@Service
@Transactional
public class ProfileAuditService extends BaseAuditService<Profile, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ProfileAuditService.class);

    private final ProfileRepository profileRepository;

    public ProfileAuditService(ProfileRepository profileRepository) {
        // Pass repository to parent service
        super(profileRepository);
        this.profileRepository = profileRepository;
    }

    /**
     * Create a new profile with audit trail.
     * Automatically captures createdBy from AuditContext.
     * 
     * @param userId User ID who owns this profile
     * @param dateOfBirth User's date of birth
     * @param about About text
     * @return Created profile with audit fields populated
     */
    public Profile createProfile(Long userId, LocalDate dateOfBirth, String about) {
        logger.info("Creating profile for user: {}", userId);
        
        Profile profile = new Profile();
        profile.setDateOfBirth(dateOfBirth);
        profile.setAbout(about);
        profile.setIsActive(true);
        profile.setProfileCompleted(false);
        
        // Save with audit - createdBy automatically set from AuditContext
        Profile created = saveWithAudit(profile);
        
        logger.info("Profile created with ID: {} by user: {}", 
            created.getId(), created.getCreatedBy());
        
        return created;
    }

    /**
     * Update profile with audit trail.
     * Automatically sets updatedBy from AuditContext.
     * 
     * @param profileId Profile ID to update
     * @param about Updated about text
     * @return Updated profile
     */
    public Optional<Profile> updateAbout(Long profileId, String about) {
        logger.info("Updating profile {} about text", profileId);
        
        Optional<Profile> optional = profileRepository.findById(profileId);
        
        if (optional.isPresent()) {
            Profile profile = optional.get();
            profile.setAbout(about);
            
            // Save - updatedBy automatically set from AuditContext
            Long userId = AuditContext.getCurrentUserId();
            logger.info("Profile {} updated about by user: {}", profileId, userId);
            
            return Optional.of(saveWithAudit(profile));
        }
        
        return Optional.empty();
    }

    /**
     * Mark profile as complete with audit trail.
     * 
     * @param profileId Profile ID
     * @return Updated profile
     */
    public Optional<Profile> completeProfile(Long profileId) {
        logger.info("Marking profile {} as complete", profileId);
        
        Optional<Profile> optional = profileRepository.findById(profileId);
        
        if (optional.isPresent()) {
            Profile profile = optional.get();
            profile.setProfileCompleted(true);
            
            return Optional.of(saveWithAudit(profile));
        }
        
        return Optional.empty();
    }

    /**
     * Soft delete profile with audit trail and reason.
     * Sets deletedAt, deletedBy, and deletion reason.
     * 
     * @param profileId Profile ID to delete
     * @param reason Reason for deletion
     * @return Soft-deleted profile
     */
    public Optional<Profile> deleteProfile(Long profileId, String reason) {
        logger.info("Soft deleting profile: {} reason: {}", profileId, reason);
        
        Optional<Profile> deleted = softDelete(profileId, reason);
        
        if (deleted.isPresent()) {
            logger.info("Profile {} soft deleted by user: {}",
                profileId, deleted.get().getDeletedBy());
        }
        
        return deleted;
    }

    /**
     * Restore a previously soft-deleted profile.
     * 
     * @param profileId Profile ID to restore
     * @return Restored profile
     */
    public Optional<Profile> restoreProfile(Long profileId) {
        logger.info("Restoring profile: {}", profileId);
        
        Optional<Profile> restored = restore(profileId);
        
        if (restored.isPresent()) {
            logger.info("Profile {} restored by user: {}",
                profileId, restored.get().getUpdatedBy());
        }
        
        return restored;
    }

    /**
     * Get active profiles (not soft-deleted).
     * 
     * @return List of active profiles
     */
    public List<Profile> getActiveProfiles() {
        logger.debug("Fetching all active profiles");
        return profileRepository.findByIsDeletedFalse();
    }

    /**
     * Get active profiles for a specific user.
     * 
     * @param userId User ID
     * @return List of active profiles owned by user
     */
    public Optional<Profile> getActiveProfileByUserId(Long userId) {
        logger.debug("Fetching active profile for user: {}", userId);
        return profileRepository.findByUser_IdAndIsDeletedFalse(userId);
    }

    /**
     * Get profiles created by a specific user (admin view).
     * 
     * @param userId User ID who created profiles
     * @return List of profiles created by this user
     */
    public List<Profile> getProfilesCreatedBy(Long userId) {
        logger.debug("Fetching profiles created by user: {}", userId);
        return profileRepository.findByCreatedBy(userId);
    }

    /**
     * Get profiles updated by a specific user.
     * 
     * @param userId User ID who updated profiles
     * @return List of profiles updated by this user
     */
    public List<Profile> getProfilesUpdatedBy(Long userId) {
        logger.debug("Fetching profiles updated by user: {}", userId);
        return profileRepository.findByUpdatedBy(userId);
    }

    /**
     * Get audit trail for a profile.
     * 
     * @param profileId Profile ID
     * @return Formatted audit trail
     */
    public Optional<String> getProfileAuditTrail(Long profileId) {
        logger.debug("Fetching audit trail for profile: {}", profileId);
        return getAuditTrail(profileId);
    }

    /**
     * Check if profile is deleted.
     * 
     * @param profileId Profile ID
     * @return true if soft-deleted, false otherwise
     */
    public boolean isProfileDeleted(Long profileId) {
        return isDeleted(profileId);
    }

    /**
     * Get complete audit information about a profile including user names.
     * 
     * @param profileId Profile ID
     * @return Detailed audit information
     */
    public Optional<ProfileAuditInfo> getDetailedAuditInfo(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        
        if (profile.isEmpty()) {
            return Optional.empty();
        }
        
        Profile p = profile.get();
        
        return Optional.of(ProfileAuditInfo.builder()
            .profileId(profileId)
            .createdAt(p.getCreatedAt())
            .createdBy(p.getCreatedBy())
            .updatedAt(p.getUpdatedAt())
            .updatedBy(p.getUpdatedBy())
            .isDeleted(p.isDeleted())
            .deletedAt(p.getDeletedAt())
            .deletedBy(p.getDeletedBy())
            .deletionReason(p.getDeletionReason())
            .version(p.getVersion())
            .auditTrail(p.getAuditTrail())
            .build());
    }

    /**
     * Internal DTO for detailed audit information.
     */
    public static class ProfileAuditInfo {
        public Long profileId;
        public java.time.LocalDateTime createdAt;
        public Long createdBy;
        public java.time.LocalDateTime updatedAt;
        public Long updatedBy;
        public Boolean isDeleted;
        public java.time.LocalDateTime deletedAt;
        public Long deletedBy;
        public String deletionReason;
        public Long version;
        public String auditTrail;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long profileId;
            private java.time.LocalDateTime createdAt;
            private Long createdBy;
            private java.time.LocalDateTime updatedAt;
            private Long updatedBy;
            private Boolean isDeleted;
            private java.time.LocalDateTime deletedAt;
            private Long deletedBy;
            private String deletionReason;
            private Long version;
            private String auditTrail;

            public Builder profileId(Long val) { this.profileId = val; return this; }
            public Builder createdAt(java.time.LocalDateTime val) { this.createdAt = val; return this; }
            public Builder createdBy(Long val) { this.createdBy = val; return this; }
            public Builder updatedAt(java.time.LocalDateTime val) { this.updatedAt = val; return this; }
            public Builder updatedBy(Long val) { this.updatedBy = val; return this; }
            public Builder isDeleted(Boolean val) { this.isDeleted = val; return this; }
            public Builder deletedAt(java.time.LocalDateTime val) { this.deletedAt = val; return this; }
            public Builder deletedBy(Long val) { this.deletedBy = val; return this; }
            public Builder deletionReason(String val) { this.deletionReason = val; return this; }
            public Builder version(Long val) { this.version = val; return this; }
            public Builder auditTrail(String val) { this.auditTrail = val; return this; }

            public ProfileAuditInfo build() {
                ProfileAuditInfo info = new ProfileAuditInfo();
                info.profileId = this.profileId;
                info.createdAt = this.createdAt;
                info.createdBy = this.createdBy;
                info.updatedAt = this.updatedAt;
                info.updatedBy = this.updatedBy;
                info.isDeleted = this.isDeleted;
                info.deletedAt = this.deletedAt;
                info.deletedBy = this.deletedBy;
                info.deletionReason = this.deletionReason;
                info.version = this.version;
                info.auditTrail = this.auditTrail;
                return info;
            }
        }
    }
}

