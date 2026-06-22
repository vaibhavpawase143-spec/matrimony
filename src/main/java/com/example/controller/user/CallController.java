package com.example.controller.user;

import com.example.dto.request.CallRequestDTO;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CallController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    @MessageMapping("/call")
    public void call(
            CallRequestDTO request,
            Principal principal
    ){

        System.out.println("================================");
        System.out.println("CALL RECEIVED");
        System.out.println("Receiver Id = " + request.getReceiverId());
        System.out.println("Type = " + request.getType());
        System.out.println("================================");

        String callerEmail =
                principal.getName();

        String receiverEmail =
                userRepository
                        .findById(request.getReceiverId())
                        .orElseThrow()
                        .getEmail();

        Map<String,Object> payload =
                new HashMap<>();

        Long callerId =
                userRepository
                        .findByEmail(callerEmail)
                        .orElseThrow()
                        .getId();

        payload.put(
                "caller",
                callerEmail
        );

        payload.put(
                "callerId",
                callerId
        );

        payload.put(
                "type",
                request.getType()
        );


        messagingTemplate.convertAndSendToUser(
                receiverEmail,
                "/queue/calls",
                payload
        );
    }
}