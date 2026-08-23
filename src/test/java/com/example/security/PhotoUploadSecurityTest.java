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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 4 — Hardened Photo Upload Security & Storage Regression Tests")
class PhotoUploadSecurityTest extends BaseIntegrationTest {

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
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, width, height);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    private byte[] createValidWebpBytes() {
        // Minimal valid 1x1 WebP VP8 binary
        return new byte[] {
                'R', 'I', 'F', 'F',
                0x1a, 0x00, 0x00, 0x00, // file size - 8
                'W', 'E', 'B', 'P',
                'V', 'P', '8', ' ',
                0x0e, 0x00, 0x00, 0x00, // chunk size
                (byte) 0x30, 0x01, 0x00, (byte) 0x9d, 0x01, 0x2a,
                0x01, 0x00, 0x01, 0x00, // 1x1 width, height
                0x02, 0x00, 0x34, 0x25
        };
    }

    private byte[] createJpegWithExifMetadata() throws Exception {
        byte[] normalJpeg = createValidImageBytes("jpeg", 60, 60);
        // Insert APP1 (Exif) marker right after SOI marker (0xFF, 0xD8)
        byte[] exifTag = "Exif\u0000\u0000GPSInfo\u0000\u0000Latitude=37.7749".getBytes(StandardCharsets.UTF_8);
        int app1Length = exifTag.length + 2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(normalJpeg[0]); // 0xFF
        baos.write(normalJpeg[1]); // 0xD8
        baos.write(0xFF);          // APP1 marker
        baos.write(0xE1);
        baos.write((app1Length >> 8) & 0xFF);
        baos.write(app1Length & 0xFF);
        baos.write(exifTag);
        baos.write(normalJpeg, 2, normalJpeg.length - 2);
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
        user.setFirstName("Hardened");
        user.setLastName("Tester");
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
    // 1. REAL IMAGE CONTENT VALIDATION (MAGIC BYTES / DECODING)
    // =========================================================================

    @Test
    @DisplayName("1. Hardened: Non-image content with spoofed image/png MIME type is strictly rejected")
    void testUpload_MimeSpoofing_Rejected() throws Exception {
        String email = "hardened_mime_user@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] fakeContent = "<html><script>alert('xss')</script></html>".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "spoofed.png",
                MediaType.IMAGE_PNG_VALUE,
                fakeContent
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("2. Hardened: Non-image payload named malicious.jpg is strictly rejected")
    void testUpload_FakeImageExtension_Rejected() throws Exception {
        String email = "hardened_fake_ext@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] fakeBinary = "MZ\u0090\u0000\u0003\u0000\u0000\u0000Fake executable bytes".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "malicious.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                fakeBinary
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("3. Hardened: Valid JPEG (< 1 MB) is accepted and assigned a server UUID")
    void testUpload_ValidJpeg_AcceptedWithUuid() throws Exception {
        String email = "hardened_valid_jpeg@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] jpegBytes = createValidImageBytes("jpeg", 100, 100);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "my_photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                jpegBytes
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").isNotEmpty())
                .andExpect(jsonPath("$.primaryPhoto").value(true));

        Optional<UserPhoto> savedPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId());
        assertTrue(savedPhoto.isPresent(), "Photo record must be saved in database");
        String photoUrl = savedPhoto.get().getPhotoUrl();
        String storedFileName = extractFileNameFromUrl(photoUrl);
        createdFilesToCleanup.add(storedFileName);

        File physicalFile = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + storedFileName);
        assertTrue(physicalFile.exists(), "Photo must physically exist on disk");
        assertFalse(photoUrl.contains("my_photo.jpg"), "Original filename must NOT be used directly");
    }

    @Test
    @DisplayName("4. Hardened: Valid PNG (< 1 MB) is accepted")
    void testUpload_ValidPng_Accepted() throws Exception {
        String email = "hardened_valid_png@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] pngBytes = createValidImageBytes("png", 80, 80);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "gallery.png",
                MediaType.IMAGE_PNG_VALUE,
                pngBytes
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "OTHER")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").isNotEmpty());
    }

    @Test
    @DisplayName("5. Hardened: Valid WEBP (< 1 MB) is accepted")
    void testUpload_ValidWebp_Accepted() throws Exception {
        String email = "hardened_valid_webp@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] webpBytes = createValidWebpBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.webp",
                "image/webp",
                webpBytes
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").isNotEmpty());

        userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId())
                .ifPresent(p -> createdFilesToCleanup.add(extractFileNameFromUrl(p.getPhotoUrl())));
    }

    // =========================================================================
    // 2. STRICT 1 MB SIZE LIMIT (<= 1MB ACCEPTED, > 1MB REJECTED)
    // =========================================================================

    @Test
    @DisplayName("6. Hardened: Photo exceeding 1 MB is strictly rejected")
    void testUpload_Exceeding1MB_Rejected() throws Exception {
        String email = "hardened_oversize_1mb@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        // 1.2 MB oversized content
        byte[] oversizedBytes = new byte[(int) (1.2 * 1024 * 1024)];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large.jpg",
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
    @DisplayName("7. Hardened: Valid photo below 1 MB is accepted")
    void testUpload_Below1MB_Accepted() throws Exception {
        String email = "hardened_below_1mb@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] smallJpeg = createValidImageBytes("jpeg", 60, 60);
        assertTrue(smallJpeg.length < 1024 * 1024, "Image must be under 1 MB");

        MockMultipartFile file = new MockMultipartFile("file", "small.jpg", MediaType.IMAGE_JPEG_VALUE, smallJpeg);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId())
                .ifPresent(p -> createdFilesToCleanup.add(extractFileNameFromUrl(p.getPhotoUrl())));
    }

    // =========================================================================
    // 3. PATH TRAVERSAL HARDENING (VIEW, DELETE, UPDATE, ABSOLUTE PATHS)
    // =========================================================================

    @Test
    @DisplayName("8. Hardened: Path traversal in GET /api/image/view/ is blocked")
    void testImageView_PathTraversal_Blocked() throws Exception {
        mockMvc.perform(get("/api/image/view/../../secret.properties"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("9. Hardened: Path traversal in DELETE /api/image/delete/ is blocked")
    void testImageDelete_PathTraversal_Blocked() throws Exception {
        String email = "hardened_del_user@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(delete("/api/image/delete/../../secret.properties")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("10. Hardened: Path traversal in PUT /api/image/update/ is blocked")
    void testImageUpdate_PathTraversal_Blocked() throws Exception {
        String email = "hardened_update_user@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] img = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", MediaType.IMAGE_JPEG_VALUE, img);

        mockMvc.perform(multipart("/api/image/update/../../secret.properties")
                        .file(file)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("11. Hardened: Absolute Windows and Unix path attempts in view are blocked")
    void testImageView_AbsolutePath_Blocked() throws Exception {
        // Windows absolute path attempt
        mockMvc.perform(get("/api/image/view/C:/Windows/win.ini"))
                .andExpect(status().is4xxClientError());

        // Unix absolute path attempt
        mockMvc.perform(get("/api/image/view//etc/passwd"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("12. Hardened: Filename traversal (../../cmd.jpg) does not escape upload directory")
    void testFilenameTraversal_NeutralizedByUuid() throws Exception {
        String email = "hardened_traversal_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] imgBytes = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile traversalFile = new MockMultipartFile(
                "file",
                "../../secret_test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imgBytes
        );

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(traversalFile)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").isNotEmpty());

        File outsideFile = new File("secret_test.jpg");
        assertFalse(outsideFile.exists(), "File must NOT have escaped upload directory");

        userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId())
                .ifPresent(p -> createdFilesToCleanup.add(extractFileNameFromUrl(p.getPhotoUrl())));
    }

    // =========================================================================
    // 4. PHOTO ACCESS AUTHORIZATION & OWNERSHIP
    // =========================================================================

    @Test
    @DisplayName("13. Hardened: Unauthorized user cannot view another user's private gallery photo")
    void testAccess_UnauthorizedPrivatePhoto_Blocked() throws Exception {
        User victim = getOrCreateTestUser("victim_private_photo@example.com");
        User attacker = getOrCreateTestUser("attacker_private_photo@example.com");

        byte[] imgBytes = createValidImageBytes("jpeg", 60, 60);
        String victimToken = TestSecurityUtils.generateTestToken(jwtUtil, victim.getEmail(), "ROLE_USER", "USER");
        String attackerToken = TestSecurityUtils.generateTestToken(jwtUtil, attacker.getEmail(), "ROLE_USER", "USER");

        // Victim first uploads primary profile photo
        MockMultipartFile profileFile = new MockMultipartFile("file", "prof.jpg", MediaType.IMAGE_JPEG_VALUE, imgBytes);
        mockMvc.perform(multipart("/api/photos/upload")
                        .file(profileFile)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + victimToken))
                .andExpect(status().isOk());

        // Victim uploads secondary private gallery photo
        MockMultipartFile galleryFile = new MockMultipartFile("file", "gallery.jpg", MediaType.IMAGE_JPEG_VALUE, imgBytes);
        mockMvc.perform(multipart("/api/photos/upload")
                        .file(galleryFile)
                        .param("type", "OTHER")
                        .header("Authorization", "Bearer " + victimToken))
                .andExpect(status().isOk());

        UserPhoto galleryPhoto = userPhotoRepository.findByUserIdAndPhotoType(victim.getId(), PhotoType.OTHER).get(0);
        String fileName = extractFileNameFromUrl(galleryPhoto.getPhotoUrl());
        createdFilesToCleanup.add(fileName);

        // 1. Attacker (free user) attempt to view private gallery photo -> 403 Forbidden
        mockMvc.perform(get("/api/image/view/" + fileName)
                        .header("Authorization", "Bearer " + attackerToken))
                .andExpect(status().isForbidden());

        // 2. Anonymous attempt to view private gallery photo -> 403 Forbidden
        mockMvc.perform(get("/api/image/view/" + fileName))
                .andExpect(status().isForbidden());

        // 3. Victim (owner) viewing own private gallery photo -> 200 OK
        mockMvc.perform(get("/api/image/view/" + fileName)
                        .header("Authorization", "Bearer " + victimToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("14. Hardened: Legitimate public profile photo access works for everyone")
    void testPublicAccess_ProfilePhoto_Accessible() throws Exception {
        String email = "hardened_public_user@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] img = createValidImageBytes("jpeg", 50, 50);
        MockMultipartFile file = new MockMultipartFile("file", "pub.jpg", MediaType.IMAGE_JPEG_VALUE, img);

        String uploadResp = mockMvc.perform(multipart("/api/image/upload")
                        .file(file)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String fileName = uploadResp.trim().substring(uploadResp.trim().lastIndexOf("/") + 1);
        createdFilesToCleanup.add(fileName);

        // Unauthenticated request to /api/image/view/{fileName} -> 200 OK
        mockMvc.perform(get("/api/image/view/" + fileName))
                .andExpect(status().isOk());

        // Unauthenticated request to /uploads/{fileName} -> 200 OK
        mockMvc.perform(get("/uploads/" + fileName))
                .andExpect(status().isOk());
    }

    // =========================================================================
    // 5. CORRUPTED IMAGES & EXIF METADATA STRIPPING
    // =========================================================================

    @Test
    @DisplayName("15. Hardened: Corrupted image stream is rejected by ImageIO decoding")
    void testUpload_CorruptedImageData_Rejected() throws Exception {
        String email = "hardened_corrupted_user@example.com";
        getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] corruptedData = new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00, 0x12, 0x34 }; // truncated JPG header
        MockMultipartFile file = new MockMultipartFile("file", "corrupt.jpg", MediaType.IMAGE_JPEG_VALUE, corruptedData);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("16. Hardened: EXIF metadata (GPS/camera details) is removed on upload")
    void testExif_MetadataStrippedOnUpload() throws Exception {
        String email = "hardened_exif_user@example.com";
        User user = getOrCreateTestUser(email);
        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        byte[] jpegWithExif = createJpegWithExifMetadata();
        String rawJpegString = new String(jpegWithExif, StandardCharsets.ISO_8859_1);
        assertTrue(rawJpegString.contains("GPSInfo"), "Input file must have GPS/Exif metadata before upload");

        MockMultipartFile file = new MockMultipartFile("file", "exif_photo.jpg", MediaType.IMAGE_JPEG_VALUE, jpegWithExif);

        mockMvc.perform(multipart("/api/photos/upload")
                        .file(file)
                        .param("type", "PROFILE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        UserPhoto savedPhoto = userPhotoRepository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId()).orElseThrow();
        String storedFileName = extractFileNameFromUrl(savedPhoto.getPhotoUrl());
        createdFilesToCleanup.add(storedFileName);

        File physicalFile = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + storedFileName);
        assertTrue(physicalFile.exists(), "Stored photo must exist on disk");

        byte[] savedBytes = Files.readAllBytes(physicalFile.toPath());
        String savedString = new String(savedBytes, StandardCharsets.ISO_8859_1);

        assertFalse(savedString.contains("GPSInfo"), "Stored photo must NOT contain GPSInfo EXIF metadata");
        assertFalse(savedString.contains("Latitude"), "Stored photo must NOT contain Latitude EXIF metadata");

        BufferedImage reDecoded = ImageIO.read(physicalFile);
        assertNotNull(reDecoded, "Stored photo without EXIF must remain a decodable valid image");
    }
}

