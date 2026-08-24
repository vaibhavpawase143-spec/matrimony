package com.example.service;

import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.serviceimpl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TopMatchesAndProfileResolutionTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private com.example.repository.UserPhotoRepository userPhotoRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User currentUser;
    private Profile currentProfile;
    private User otherUser;
    private Profile otherProfile;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(101L);
        currentUser.setEmail("priya@example.com");
        currentUser.setFirstName("Priya");
        currentUser.setLastName("Sharma");
        currentUser.setIsActive(true);
        currentUser.setIsDeleted(false);
        currentUser.setIsBlocked(false);

        currentProfile = Profile.builder()
                .id(11L)
                .user(currentUser)
                .isActive(true)
                .profileCompleted(true)
                .aboutMe("Looking for a life partner")
                .build();
        currentUser.setProfile(currentProfile);

        otherUser = new User();
        otherUser.setId(202L);
        otherUser.setEmail("rahul@example.com");
        otherUser.setFirstName("Rahul");
        otherUser.setLastName("Verma");
        otherUser.setIsActive(true);
        otherUser.setIsDeleted(false);
        otherUser.setIsBlocked(false);

        otherProfile = Profile.builder()
                .id(55L)
                .user(otherUser)
                .isActive(true)
                .profileCompleted(true)
                .aboutMe("Software Engineer in Pune")
                .build();
        otherUser.setProfile(otherProfile);
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("MATCH-1: Profile lookup succeeds directly by profileId")
    void testGetProfileByProfileIdDirectly() {
        mockSecurityContext("priya@example.com");
        when(userRepository.findByEmailIgnoreCase("priya@example.com")).thenReturn(Optional.of(currentUser));
        when(profileRepository.findByUserIdWithDetails(101L)).thenReturn(Optional.of(currentProfile));
        when(profileRepository.findByProfileIdWithRelations(55L)).thenReturn(Optional.of(otherProfile));

        ProfileResponseDTO dto = profileService.getProfileById(55L);

        assertNotNull(dto);
        assertEquals(202L, dto.getUserId());
        verify(profileRepository, times(1)).findByProfileIdWithRelations(55L);
    }

    @Test
    @DisplayName("MATCH-2: Profile lookup seamlessly falls back to userId when profileId not found")
    void testGetProfileByUserIdFallback() {
        mockSecurityContext("priya@example.com");
        when(userRepository.findByEmailIgnoreCase("priya@example.com")).thenReturn(Optional.of(currentUser));
        when(profileRepository.findByUserIdWithDetails(101L)).thenReturn(Optional.of(currentProfile));
        // profileId lookup returns empty for 202L
        when(profileRepository.findByProfileIdWithRelations(202L)).thenReturn(Optional.empty());
        // userId lookup succeeds for 202L
        when(profileRepository.findByUserIdWithDetails(202L)).thenReturn(Optional.of(otherProfile));

        ProfileResponseDTO dto = profileService.getProfileById(202L);

        assertNotNull(dto);
        assertEquals(202L, dto.getUserId());
        verify(profileRepository, times(1)).findByProfileIdWithRelations(202L);
        verify(profileRepository, times(1)).findByUserIdWithDetails(202L);
    }

    @Test
    @DisplayName("MATCH-3: Profile lookup throws when neither profileId nor userId exists")
    void testGetProfileNotFound() {
        mockSecurityContext("priya@example.com");
        when(userRepository.findByEmailIgnoreCase("priya@example.com")).thenReturn(Optional.of(currentUser));
        when(profileRepository.findByProfileIdWithRelations(999L)).thenReturn(Optional.empty());
        when(profileRepository.findByUserIdWithDetails(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profileService.getProfileById(999L));
    }

    @Test
    @DisplayName("MATCH-4: Profile lookup rejects inactive or blocked user profile")
    void testGetProfileBlockedOrDeleted() {
        mockSecurityContext("priya@example.com");
        when(userRepository.findByEmailIgnoreCase("priya@example.com")).thenReturn(Optional.of(currentUser));

        User blockedUser = new User();
        blockedUser.setId(303L);
        blockedUser.setEmail("blocked@example.com");
        blockedUser.setIsActive(true);
        blockedUser.setIsDeleted(false);
        blockedUser.setIsBlocked(true);

        Profile blockedProfile = Profile.builder()
                .id(77L)
                .user(blockedUser)
                .isActive(true)
                .build();

        when(profileRepository.findByProfileIdWithRelations(77L)).thenReturn(Optional.of(blockedProfile));

        assertThrows(RuntimeException.class, () -> profileService.getProfileById(77L));
    }
}
