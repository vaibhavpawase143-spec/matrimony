package com.example.controller.user;

import com.example.dto.request.KundliMatchRequestDTO;
import com.example.dto.response.KundliMatchResponseDTO;
import com.example.service.KundliService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kundli")
@RequiredArgsConstructor
@CrossOrigin("*")
public class KundliController {

    private final KundliService kundliService;

    @PostMapping("/match")
    public ResponseEntity<KundliMatchResponseDTO> match(
            @RequestBody KundliMatchRequestDTO request) {

        return ResponseEntity.ok(kundliService.match(request));
    }
}