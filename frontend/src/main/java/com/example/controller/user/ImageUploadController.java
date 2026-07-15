package com.example.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/image")
public class ImageUploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // ===================== UPLOAD =====================
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        try {
            // ✅ Empty check
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // ✅ Size check
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("File size exceeds 5MB");
            }

            // ✅ Type check
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.equals(MediaType.IMAGE_JPEG_VALUE) &&
                            !contentType.equals(MediaType.IMAGE_PNG_VALUE))) {
                return ResponseEntity.badRequest().body("Only JPG and PNG allowed");
            }

            // 📁 Create folder if not exists
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 🧠 Clean + FIX filename (🔥 IMPORTANT FIX)
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename())
                    .replaceAll("\\s+", "_")     // remove spaces
                    .replaceAll("[()]", "");     // remove brackets

            if (originalFileName.contains("..")) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }

            // 🆔 Unique filename
            String fileName = System.currentTimeMillis() + "_" + originalFileName;

            // 📦 Save file
            File dest = new File(directory, fileName);
            file.transferTo(dest);

            // 🌐 Return API URL
            String fileUrl = "/api/image/view/" + fileName;

            return ResponseEntity.ok(fileUrl);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    // ===================== VIEW =====================
    @GetMapping("/view/{fileName}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String fileName) {

        try {
            File file = new File(UPLOAD_DIR + fileName);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] imageBytes = Files.readAllBytes(file.toPath());

            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===================== DELETE =====================
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {

        try {
            File file = new File(UPLOAD_DIR + fileName);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            boolean deleted = file.delete();

            if (deleted) {
                return ResponseEntity.ok("Image deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to delete image");
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Delete failed: " + e.getMessage());
        }
    }

    // ===================== UPDATE =====================
    @PutMapping("/update/{oldFileName}")
    public ResponseEntity<?> updateImage(
            @PathVariable String oldFileName,
            @RequestParam("file") MultipartFile newFile) {

        try {
            // ✅ Validation
            if (newFile == null || newFile.isEmpty()) {
                return ResponseEntity.badRequest().body("New file is empty");
            }

            if (newFile.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("File size exceeds 5MB");
            }

            String contentType = newFile.getContentType();
            if (contentType == null ||
                    (!contentType.equals(MediaType.IMAGE_JPEG_VALUE) &&
                            !contentType.equals(MediaType.IMAGE_PNG_VALUE))) {
                return ResponseEntity.badRequest().body("Only JPG and PNG allowed");
            }

            // ❌ Delete old file
            File oldFile = new File(UPLOAD_DIR + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }

            // 🧠 Clean filename (same fix)
            String originalFileName = StringUtils.cleanPath(newFile.getOriginalFilename())
                    .replaceAll("\\s+", "_")
                    .replaceAll("[()]", "");

            String newFileName = System.currentTimeMillis() + "_" + originalFileName;

            // 📦 Save new file
            File dest = new File(UPLOAD_DIR, newFileName);
            newFile.transferTo(dest);

            String fileUrl = "/api/image/view/" + newFileName;

            return ResponseEntity.ok(fileUrl);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Update failed: " + e.getMessage());
        }
    }
}