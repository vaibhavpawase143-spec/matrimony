package com.example.controller.user;

import com.example.dto.response.ProfileVisitorResponseDTO;
import com.example.service.ProfileVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-visitors")
@RequiredArgsConstructor
public class ProfileVisitorController {

    private final ProfileVisitorService service;

    @PostMapping("/{visitedUserId}")
    public ResponseEntity<?> saveVisit(
            @PathVariable Long visitedUserId
    ) {

        service.saveVisit(
                visitedUserId
        );

        return ResponseEntity.ok().build();

    }

    @GetMapping("/me")
    public List<ProfileVisitorResponseDTO>
    getMyVisitors() {

        return service.getMyVisitors();

    }
}