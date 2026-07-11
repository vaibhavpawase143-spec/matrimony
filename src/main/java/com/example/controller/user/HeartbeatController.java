package com.example.controller.user;

import com.example.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/heartbeat")
@RequiredArgsConstructor
public class HeartbeatController {

    private final OnlineUserService onlineUserService;

    @PostMapping
    public ResponseEntity<Void> heartbeat(
            Principal principal
    ) {

        onlineUserService.heartbeat(
                principal.getName()
        );

        return ResponseEntity.ok().build();

    }

}