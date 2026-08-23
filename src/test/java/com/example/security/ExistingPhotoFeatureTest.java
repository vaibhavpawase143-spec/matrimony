package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
import com.example.model.PhotoType;
import com.example.model.Profile;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.repository.ProfileRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;
import com.example.service.FileStorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Existing Profile & User Photo Upload Functionality Tests")
class ExistingPhotoFeatureTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final List<String> createdFilesToCleanup = new ArrayList<>();

    @AfterEach
    void cleanupUploadedFiles() {
        for (String fileName : createdFilesToCleanup) {
            try {
                fileStorageService.deleteFile(fileName);
            } catch (Exception ignored) {}
        }
        createdFilesToCleanup.clear();
    }

    private byte[] createValidImageBytes(String format, int width, int height) throws Exception {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, width, height);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    private User getOrCreateTestUser(String email) {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Photo");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);

        // Ensure clean photo state for test idempotency
        List<UserPhoto> existingPhotos = userPhotoRepository.findByUserId(savedUser.getId());
        userPhotoRepository.deleteAll(existingPhotos);

        // Ensure profile exists
        profileRepository.findByUser(savedUser).orElseGet(() -> {
            Profile p = new Profile();
            p.setUser(savedUser);
            return profileRepository.save(p);
        });

        return savedUser;
    }

    private String extractFileNameFromUrl(String photoUrl) {
        if (photoUrl == null || !photoUrl.contains("/")) return "";
        return photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
    }

    // =========================================================================
    // 1. AUTHENTICATED SINGLE UPLOAD & STORAGE VERIFICATION
    // =========================================================================

    @Test
    @DisplayName("1. Authenticated user can upload a valid supported photo and receive expected response")
    void testUpload_AuthenticatedUser_ValidPhoto_Success() throws Exception {
        String email = "existing_photo_user1@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] jpegBytes = createValidImageBytes("jpeg", 80, 80);
        MockMultipartFile file = new MockMultipartFile("file", "my_avatar.jpg", MediaType.IMAGE_JPEG_VALUE, jpegBytes);

        String responseBody = mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").isNotEmpty())
                .andExpect(jsonPath("$.photoType").value("PROFILE"))
                .andExpect(jsonPath("$.primaryPhoto").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify uploaded file actually exists in storage directory
        Optional<UserPhoto> savedPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId());
        assertTrue(savedPhoto.isPresent(), "Photo record must be saved in database");
        String photoUrl = savedPhoto.get().getPhotoUrl();
        String storedFileName = extractFileNameFromUrl(photoUrl);
        createdFilesToCleanup.add(storedFileName);

        File physicalFile = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + storedFileName);
        assertTrue(physicalFile.exists(), "Uploaded file must physically exist in storage directory");

        // Verify profile record was updated with image URL
        Profile profile = profileRepository.findByUser(user).orElseThrow();
        assertEquals(photoUrl, profile.getImageUrl(), "User Profile imageUrl must be updated to new photo URL");
    }

    // =========================================================================
    // 2. MULTIPLE UPLOAD (GALLERY)
    // =========================================================================

    @Test
    @DisplayName("2. Authenticated user can upload multiple photos in gallery")
    void testUploadMultiple_GalleryPhotos_Success() throws Exception {
        String email = "existing_multi_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] img1 = createValidImageBytes("jpeg", 50, 50);
        byte[] img2 = createValidImageBytes("png", 50, 50);

        MockMultipartFile file1 = new MockMultipartFile("files", "g1.jpg", MediaType.IMAGE_JPEG_VALUE, img1);
        MockMultipartFile file2 = new MockMultipartFile("files", "g2.png", MediaType.IMAGE_PNG_VALUE, img2);

        mockMvc.perform(multipart("/api/photos/upload-multiple")
                        .file(file1)
                        .file(file2)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        List<UserPhoto> photos = userPhotoRepository.findAll();
        for (UserPhoto p : photos) {
            if (p.getUser().getId().equals(user.getId())) {
                createdFilesToCleanup.add(extractFileNameFromUrl(p.getPhotoUrl()));
            }
        }
    }

    // =========================================================================
    // 3. PHOTO REPLACEMENT & GALLERY HANDLING
    // =========================================================================

    @Test
    @DisplayName("3. Uploading new profile photo sets new primary and updates Profile.imageUrl")
    void testUpload_ReplaceProfilePhoto_UpdatesPrimaryAndProfile() throws Exception {
        String email = "existing_replace_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        // First upload
        byte[] img1 = createValidImageBytes("jpeg", 60, 60);
        MockMultipartFile file1 = new MockMultipartFile("file", "first.jpg", MediaType.IMAGE_JPEG_VALUE, img1);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file1)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        UserPhoto firstPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId()).orElseThrow();
        String firstFileName = extractFileNameFromUrl(firstPhoto.getPhotoUrl());
        createdFilesToCleanup.add(firstFileName);

        // Second upload with PROFILE type
        byte[] img2 = createValidImageBytes("png", 70, 70);
        MockMultipartFile file2 = new MockMultipartFile("file", "second.png", MediaType.IMAGE_PNG_VALUE, img2);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file2)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        UserPhoto secondPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId()).orElseThrow();
        String secondFileName = extractFileNameFromUrl(secondPhoto.getPhotoUrl());
        createdFilesToCleanup.add(secondFileName);

        assertNotEquals(firstFileName, secondFileName, "Second photo must have a different unique filename");

        Profile profile = profileRepository.findByUser(user).orElseThrow();
        assertEquals(secondPhoto.getPhotoUrl(), profile.getImageUrl(), "Profile imageUrl must point to the new primary photo");
    }

    // =========================================================================
    // 4. PHOTO DELETION (BY ID AND BY TYPE)
    // =========================================================================

    @Test
    @DisplayName("4. Deleting photo by ID removes record and physical file, promotes remaining photo")
    void testDeletePhoto_ById_RemovesFileAndPromotesOldest() throws Exception {
        String email = "existing_delete_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        // Upload primary photo
        byte[] img1 = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file1 = new MockMultipartFile("file", "primary.jpg", MediaType.IMAGE_JPEG_VALUE, img1);
        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file1)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        UserPhoto primaryPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId()).orElseThrow();
        String primaryFileName = extractFileNameFromUrl(primaryPhoto.getPhotoUrl());
        File primaryFile = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + primaryFileName);

        // Upload secondary photo
        byte[] img2 = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file2 = new MockMultipartFile("file", "secondary.jpg", MediaType.IMAGE_JPEG_VALUE, img2);
        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file2)
                        .param("type", "OTHER")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Delete the primary photo
        mockMvc.perform(delete("/api/photos/" + primaryPhoto.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertFalse(userPhotoRepository.findById(primaryPhoto.getId()).isPresent(), "Deleted photo must be removed from DB");
        assertFalse(primaryFile.exists(), "Physical file must be deleted from filesystem");

        // Verify remaining photo was automatically promoted to primary
        Optional<UserPhoto> remainingPrimary = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId());
        assertTrue(remainingPrimary.isPresent(), "Oldest remaining photo must be promoted to primary");
        createdFilesToCleanup.add(extractFileNameFromUrl(remainingPrimary.get().getPhotoUrl()));
    }

    @Test
    @DisplayName("5. Deleting photo by type clears profile imageUrl when type is PROFILE")
    void testDeletePhoto_ByTypeProfile_ClearsProfileImageUrl() throws Exception {
        String email = "existing_del_type_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] img = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file = new MockMultipartFile("file", "prof.jpg", MediaType.IMAGE_JPEG_VALUE, img);
        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        UserPhoto photo = userPhotoRepository.findFirstByUserIdAndPhotoType(user.getId(), PhotoType.PROFILE).orElseThrow();
        String fileName = extractFileNameFromUrl(photo.getPhotoUrl());

        mockMvc.perform(delete("/api/photos/delete")
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Profile profile = profileRepository.findByUser(user).orElseThrow();
        assertNull(profile.getImageUrl(), "Profile imageUrl must be null after deleting profile photo");

        File fileOnDisk = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + fileName);
        assertFalse(fileOnDisk.exists(), "Physical photo file must be deleted when deleting by type");
    }

    // =========================================================================
    // 5. SECURITY & AUTHORIZATION: UNAUTHENTICATED & OWNERSHIP (IDOR)
    // =========================================================================

    @Test
    @DisplayName("6. Unauthenticated upload attempt is rejected with HTTP 401")
    void testUpload_Unauthenticated_RejectedWith401() throws Exception {
        byte[] img = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file = new MockMultipartFile("file", "anon.jpg", MediaType.IMAGE_JPEG_VALUE, img);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("7. Unauthorized user cannot delete another user's photo")
    void testOwnership_UserCannotDeleteAnotherUsersPhoto() throws Exception {
        User victim = getOrCreateTestUser("existing_victim@example.com");
        User attacker = getOrCreateTestUser("existing_attacker@example.com");

        UserPhoto victimPhoto = new UserPhoto();
        victimPhoto.setUser(victim);
        victimPhoto.setPhotoType(PhotoType.PROFILE);
        victimPhoto.setPhotoUrl("http://localhost:9090/uploads/victim_photo.jpg");
        victimPhoto.setPrimaryPhoto(true);
        victimPhoto = userPhotoRepository.save(victimPhoto);

        String attackerToken = TestSecurityUtils.generateTestToken(jwtUtil, attacker.getEmail(), "ROLE_USER", "USER");

        mockMvc.perform(delete("/api/photos/" + victimPhoto.getId())
                        .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().is4xxClientError());

        assertTrue(userPhotoRepository.findById(victimPhoto.getId()).isPresent(), "Victim's photo must remain in DB");
    }

    @Test
    @DisplayName("8. Unauthorized user cannot set another user's photo as primary")
    void testOwnership_UserCannotSetAnotherUsersPhotoAsPrimary() throws Exception {
        User victim = getOrCreateTestUser("existing_victim2@example.com");
        User attacker = getOrCreateTestUser("existing_attacker2@example.com");

        UserPhoto victimPhoto = new UserPhoto();
        victimPhoto.setUser(victim);
        victimPhoto.setPhotoType(PhotoType.OTHER);
        victimPhoto.setPhotoUrl("http://localhost:9090/uploads/victim_gallery.jpg");
        victimPhoto.setPrimaryPhoto(false);
        victimPhoto = userPhotoRepository.save(victimPhoto);

        String attackerToken = TestSecurityUtils.generateTestToken(jwtUtil, attacker.getEmail(), "ROLE_USER", "USER");

        mockMvc.perform(put("/api/photos/primary/" + victimPhoto.getId())
                        .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().is4xxClientError());
    }

    // =========================================================================
    // 6. VALIDATION: UNSUPPORTED TYPES, EMPTY FILES, CONFIGURED MAX SIZE (5MB)
    // =========================================================================

    @Test
    @DisplayName("9. Unsupported file type (PDF) is rejected")
    void testUpload_UnsupportedFileType_Rejected() throws Exception {
        String email = "existing_bad_type@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        MockMultipartFile pdfFile = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "%PDF-1.4 dummy pdf bytes".getBytes()
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(pdfFile)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("10. Empty / 0-byte file is rejected")
    void testUpload_EmptyFile_Rejected() throws Exception {
        String email = "existing_empty_file@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(emptyFile)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("11. Current configured max size check (file exceeding 5MB) is rejected")
    void testUpload_Exceeding5MB_Rejected() throws Exception {
        String email = "existing_oversize5mb@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        // 6 MB dummy content
        byte[] oversizedBytes = new byte[6 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "huge.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                oversizedBytes
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("12. Non-image MIME type (text/html) is rejected")
    void testUpload_NonImageMimeType_Rejected() throws Exception {
        String email = "existing_html_content@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "fake.html",
                "text/html",
                "<html><body>Not an image</body></html>".getBytes()
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }
}
