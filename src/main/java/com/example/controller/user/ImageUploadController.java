package com.example.controller.user;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {

        try {

            String fileName = file.getOriginalFilename();

            File dest = new File(UPLOAD_DIR + fileName);

            file.transferTo(dest);

            return "File uploaded successfully: " + fileName;

        } catch (IOException e) {
            return "Upload failed";
        }
    }
}