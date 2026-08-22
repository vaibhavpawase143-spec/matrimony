package com.example.serviceimpl;

import com.example.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "uploads"
                    + File.separator;

    private static final long MAX_SIZE = 1 * 1024 * 1024; // 1 MB (1048576 bytes)

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    @Override
    public String storeFile(MultipartFile file) {
        byte[] fileBytes = validateFile(file);

        try {
            Path baseDirPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            File directory = baseDirPath.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalName = file.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            }

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("Invalid file extension. Allowed extensions: .jpg, .jpeg, .png, .webp");
            }

            String fileName = UUID.randomUUID() + extension;
            Path destinationPath = baseDirPath.resolve(fileName).normalize();

            // 🔒 PREVENT PATH TRAVERSAL
            if (!destinationPath.startsWith(baseDirPath)) {
                throw new SecurityException("Path traversal attempt detected!");
            }

            File destination = destinationPath.toFile();

            // 🔒 PRIVACY: Read image bytes and write clean decoded raster (stripping EXIF metadata such as GPS/camera details)
            String formatName = extension.replace(".", "").toLowerCase();
            if (formatName.equals("jpg")) {
                formatName = "jpeg";
            }

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
            if (image != null && (formatName.equals("jpeg") || formatName.equals("png"))) {
                boolean written = ImageIO.write(image, formatName, destination);
                if (!written) {
                    file.transferTo(destination);
                }
            } else {
                file.transferTo(destination);
            }

            return fileName;

        } catch (SecurityException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    private byte[] validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 1MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only JPEG, PNG, and WEBP images are allowed");
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image bytes");
        }

        if (bytes.length > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 1MB");
        }

        // 🔒 MAGIC BYTES / HEADER SIGNATURE VALIDATION
        if (!hasValidImageSignature(bytes)) {
            throw new IllegalArgumentException("Uploaded file is not a valid image format");
        }

        // 🔒 REAL IMAGE CONTENT VALIDATION (DECODING & CORRUPTION CHECK)
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            if (img == null || img.getWidth() <= 0 || img.getHeight() <= 0) {
                // If WEBP cannot be read by standard ImageIO, verify valid header signature
                if (!isWebpSignature(bytes)) {
                    throw new IllegalArgumentException("Uploaded file is not a valid image");
                }
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to decode image file: corrupted or invalid image data");
        }

        return bytes;
    }

    private boolean hasValidImageSignature(byte[] bytes) {
        if (bytes == null || bytes.length < 8) {
            return false;
        }

        // JPEG: FF D8 FF
        if ((bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8 && (bytes[2] & 0xFF) == 0xFF) {
            return true;
        }

        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if ((bytes[0] & 0xFF) == 0x89 &&
            (bytes[1] & 0xFF) == 0x50 &&
            (bytes[2] & 0xFF) == 0x4E &&
            (bytes[3] & 0xFF) == 0x47 &&
            (bytes[4] & 0xFF) == 0x0D &&
            (bytes[5] & 0xFF) == 0x0A &&
            (bytes[6] & 0xFF) == 0x1A &&
            (bytes[7] & 0xFF) == 0x0A) {
            return true;
        }

        // WEBP: RIFF....WEBP
        return isWebpSignature(bytes);
    }

    private boolean isWebpSignature(byte[] bytes) {
        if (bytes == null || bytes.length < 12) {
            return false;
        }
        return (bytes[0] & 0xFF) == 0x52 && // R
               (bytes[1] & 0xFF) == 0x49 && // I
               (bytes[2] & 0xFF) == 0x46 && // F
               (bytes[3] & 0xFF) == 0x46 && // F
               (bytes[8] & 0xFF) == 0x57 && // W
               (bytes[9] & 0xFF) == 0x45 && // E
               (bytes[10] & 0xFF) == 0x42 && // B
               (bytes[11] & 0xFF) == 0x50;  // P
    }

    @Override
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }

        // 🔒 PREVENT PATH TRAVERSAL CHARACTERS
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") ||
            fileName.contains("\0") || fileName.contains(":") || fileName.startsWith(".")) {
            throw new SecurityException("Path traversal attempt detected during deletion!");
        }

        try {
            Path baseDirPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path targetPath = baseDirPath.resolve(fileName).normalize();

            // 🔒 CANONICAL BOUNDARY CHECK
            if (!targetPath.startsWith(baseDirPath)) {
                throw new SecurityException("Path traversal attempt detected during deletion!");
            }

            File file = targetPath.toFile();
            if (file.exists() && !file.delete()) {
                throw new RuntimeException("Failed to delete file");
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }
}