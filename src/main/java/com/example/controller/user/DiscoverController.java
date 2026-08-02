package com.example.controller.user;

import com.example.dto.response.DiscoverProfileDTO;
import com.example.service.DiscoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discover")
@RequiredArgsConstructor
public class DiscoverController {

    private final DiscoverService discoverService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<DiscoverProfileDTO>> discover(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(

                discoverService.discoverProfiles(
                        userId,
                        PageRequest.of(page, size)
                )

        );
    }
}