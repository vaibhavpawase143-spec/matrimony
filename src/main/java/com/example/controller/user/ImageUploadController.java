package com.example.controller.user;

import com.example.model.PhotoType;
import com.example.model.UserPhoto;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;
import com.example.service.FileStorageService;
import com.example.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageUploadController {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "uploads"
                    + File.separator;

    private final FileStorageService fileStorageService;
    private final UserPhotoRepository userPhotoRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    // ===================== UPLOAD =====================
    @PostMapping(value = "/api/image/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "/api/image/view/" + fileName;
            return ResponseEntity.ok(fileUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body("Path traversal attempt detected: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    // ===================== VIEW =====================
    @GetMapping({"/api/image/view/{fileName:.+}", "/uploads/{fileName:.+}"})
    public ResponseEntity<byte[]> viewImage(@PathVariable String fileName) {
        // 🔒 CANONICAL PATH TRAVERSAL CHECK
        if (fileName == null || fileName.isBlank() ||
                fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") ||
                fileName.contains("\0") || fileName.contains(":") || fileName.startsWith(".")) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Path baseDirPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path targetPath = baseDirPath.resolve(fileName).normalize();

            if (!targetPath.startsWith(baseDirPath)) {
                return ResponseEntity.badRequest().body(null);
            }

            File file = targetPath.toFile();
            if (!file.exists() || file.isDirectory()) {
                return ResponseEntity.notFound().build();
            }

            // 🔒 PHOTO ACCESS AUTHORIZATION
            Optional<UserPhoto> photoOpt = userPhotoRepository.findFirstByPhotoUrlEndingWith(fileName);
            if (photoOpt.isEmpty()) {
                photoOpt = userPhotoRepository.findFirstByPhotoUrlContaining(fileName);
            }

            if (photoOpt.isPresent()) {
                UserPhoto userPhoto = photoOpt.get();
                boolean isPublicProfile = Boolean.TRUE.equals(userPhoto.getPrimaryPhoto()) ||
                        userPhoto.getPhotoType() == PhotoType.PROFILE;

                if (!isPublicProfile) {
                    // Non-primary gallery / kundali / private photo -> require authorization
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }

                    boolean isAdmin = auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));

                    if (!isAdmin) {
                        String currentEmail = auth.getName();
                        boolean isOwner = userPhoto.getUser() != null &&
                                currentEmail.equalsIgnoreCase(userPhoto.getUser().getEmail());

                        if (!isOwner) {
                            boolean isPremium = false;
                            try {
                                isPremium = subscriptionService.isCurrentUserPremium();
                            } catch (Exception ignored) {}

                            if (!isPremium) {
                                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                            }
                        }
                    }
                }
            }

            byte[] imageBytes = Files.readAllBytes(targetPath);
            String contentType = Files.probeContentType(targetPath);
            if (contentType == null) {
                if (fileName.toLowerCase().endsWith(".png")) {
                    contentType = MediaType.IMAGE_PNG_VALUE;
                } else if (fileName.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                } else {
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===================== DELETE =====================
    @DeleteMapping("/api/image/delete/{fileName:.+}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {
        // 🔒 CANONICAL PATH TRAVERSAL CHECK
        if (fileName == null || fileName.isBlank() ||
                fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") ||
                fileName.contains("\0") || fileName.contains(":") || fileName.startsWith(".")) {
            return ResponseEntity.badRequest().body("Path traversal attempt detected");
        }

        try {
            Path baseDirPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path targetPath = baseDirPath.resolve(fileName).normalize();

            if (!targetPath.startsWith(baseDirPath)) {
                return ResponseEntity.badRequest().body("Path traversal attempt detected");
            }

            File file = targetPath.toFile();
            if (!file.exists() || file.isDirectory()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            boolean deleted = file.delete();
            if (deleted) {
                return ResponseEntity.ok("Image deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to delete image");
            }

        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body("Path traversal attempt detected");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Delete failed: " + e.getMessage());
        }
    }

    // ===================== UPDATE =====================
    @PutMapping(value = "/api/image/update/{oldFileName:.+}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateImage(
            @PathVariable String oldFileName,
            @RequestParam("file") MultipartFile newFile) {

        // 🔒 CANONICAL PATH TRAVERSAL CHECK ON OLD FILENAME
        if (oldFileName == null || oldFileName.isBlank() ||
                oldFileName.contains("..") || oldFileName.contains("/") || oldFileName.contains("\\") ||
                oldFileName.contains("\0") || oldFileName.contains(":") || oldFileName.startsWith(".")) {
            return ResponseEntity.badRequest().body("Path traversal attempt detected");
        }

        try {
            // Store new file first (validates 1MB, magic bytes, decoding, EXIF strip)
            String newFileName = fileStorageService.storeFile(newFile);

            // 🔒 SAFE DELETION OF OLD FILE
            try {
                Path baseDirPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                Path oldPath = baseDirPath.resolve(oldFileName).normalize();
                if (oldPath.startsWith(baseDirPath)) {
                    File oldFile = oldPath.toFile();
                    if (oldFile.exists() && !oldFile.isDirectory()) {
                        oldFile.delete();
                    }
                }
            } catch (Exception ignored) {}

            String fileUrl = "/api/image/view/" + newFileName;
            return ResponseEntity.ok(fileUrl);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body("Path traversal attempt detected");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Update failed: " + e.getMessage());
        }
    }
}