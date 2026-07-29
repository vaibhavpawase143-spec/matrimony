package com.example.controller.user;

import com.example.dto.request.NotificationPreferenceRequestDTO;
import com.example.dto.response.NotificationPreferenceResponseDTO;
import com.example.service.NotificationPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification-preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService notificationPreferenceService;

    @GetMapping("/me")
    public ResponseEntity<NotificationPreferenceResponseDTO> getMyPreferences() {

        return ResponseEntity.ok(
                notificationPreferenceService.getMyPreferences()
        );
    }

    @PutMapping
    public ResponseEntity<NotificationPreferenceResponseDTO> updatePreferences(
            @Valid @RequestBody NotificationPreferenceRequestDTO request) {

        return ResponseEntity.ok(
                notificationPreferenceService.updatePreferences(request)
        );
    }
}