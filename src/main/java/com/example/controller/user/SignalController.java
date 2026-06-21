package com.example.controller.user;

import com.example.dto.request.SignalDTO;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SignalController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    @MessageMapping("/signal")
    public void signal(
            SignalDTO dto,
            Principal principal
    ) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("🚀 SIGNAL CONTROLLER HIT");
        System.out.println("======================================");

        try {

            System.out.println(
                    "PRINCIPAL = " +
                            (principal != null
                                    ? principal.getName()
                                    : "NULL")
            );

            System.out.println(
                    "DTO = " + dto
            );

            if (dto == null) {

                System.out.println(
                        "❌ DTO IS NULL"
                );

                return;
            }

            System.out.println(
                    "TARGET USER ID = " +
                            dto.getTargetUserId()
            );

            System.out.println(
                    "TYPE = " +
                            dto.getType()
            );

            String receiverEmail =
                    userRepository
                            .findById(
                                    dto.getTargetUserId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "USER NOT FOUND : "
                                                    + dto.getTargetUserId()
                                    )
                            )
                            .getEmail();

            System.out.println(
                    "RECEIVER EMAIL = " +
                            receiverEmail
            );

            messagingTemplate.convertAndSendToUser(
                    receiverEmail,
                    "/queue/signals",
                    dto
            );

            System.out.println(
                    "✅ SIGNAL SENT TO " +
                            receiverEmail
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "❌ SIGNAL ERROR"
            );

            e.printStackTrace();
        }
    }
}