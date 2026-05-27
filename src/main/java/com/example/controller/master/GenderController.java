package com.example.controller.master;

import com.example.dto.response.GenderResponseDTO;
import com.example.service.GenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GenderController {

    private final GenderService genderService;

    @GetMapping
    public ResponseEntity<List<GenderResponseDTO>> getAllGenders() {

        return ResponseEntity.ok(
                genderService.getAllGenders()
        );
    }
}