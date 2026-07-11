package com.example.serviceimpl;

import com.example.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "uploads"
                    + File.separator;

    private static final long MAX_SIZE =
            5 * 1024 * 1024; // 5 MB

    @Override
    public String storeFile(MultipartFile file) {

        validateFile(file);

        try {

            File directory =
                    new File(UPLOAD_DIR);

            if (!directory.exists()) {

                directory.mkdirs();

            }

            String originalName =
                    file.getOriginalFilename();

            String extension = "";

            if (
                    originalName != null
                            &&
                            originalName.contains(".")
            ) {

                extension =
                        originalName.substring(
                                originalName.lastIndexOf(".")
                        );

            }

            String fileName =
                    UUID.randomUUID()
                            + extension;

            File destination =
                    new File(
                            directory,
                            fileName
                    );

            System.out.println(
                    "UPLOAD_DIR = "
                            + directory.getAbsolutePath()
            );

            System.out.println(
                    "DESTINATION = "
                            + destination.getAbsolutePath()
            );

            file.transferTo(destination);

            return fileName;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "File upload failed: "
                            + e.getMessage()
            );
        }
    }

    private void validateFile(
            MultipartFile file
    ) {

        if (file.isEmpty()) {

            throw new RuntimeException(
                    "File is empty"
            );

        }

        if (file.getSize() > MAX_SIZE) {

            throw new RuntimeException(
                    "File size exceeds 5MB"
            );

        }

        String contentType =
                file.getContentType();

        // Accept ANY image type
        if (
                contentType == null
                        ||
                        !contentType.startsWith(
                                "image/"
                        )
        ) {

            throw new RuntimeException(
                    "Only image files allowed"
            );

        }
    }

    @Override
    public void deleteFile(
            String fileName
    ) {

        if (
                fileName == null
                        ||
                        fileName.isBlank()
        ) {

            return;

        }

        File file =
                new File(
                        UPLOAD_DIR + fileName
                );

        if (
                file.exists()
                        &&
                        !file.delete()
        ) {

            throw new RuntimeException(
                    "Failed to delete file"
            );

        }
    }
}