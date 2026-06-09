package com.example.controller.user;

import com.example.dto.response.SwipeResponseDTO;
import com.example.model.Swipe;
import com.example.model.SwipeType;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.SwipeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/swipes")
public class SwipeController {

    private final SwipeService swipeService;
    private final UserRepository userRepository;

    public SwipeController(
            SwipeService swipeService,
            UserRepository userRepository
    ){

        this.swipeService =
                swipeService;

        this.userRepository =
                userRepository;

    }

    private Long getCurrentUserId(){

        String email =
                SecurityUtils
                        .getCurrentUsername();

        User user =
                userRepository
                        .findByEmailWithRoles(
                                email
                        )
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "User not found"
                                )
                        );

        return user.getId();

    }

    private SwipeResponseDTO map(
            Swipe s
    ){

        return SwipeResponseDTO
                .builder()

                .id(
                        s.getId()
                )

                .fromUserId(
                        s.getFromUser()
                                .getId()
                )

                .toUserId(
                        s.getToUser()
                                .getId()
                )

                .type(
                        s.getType()
                                .name()
                )

                .build();

    }

    @PostMapping(
            "/like/{userId}"
    )
    public ResponseEntity<SwipeResponseDTO>
    likeUser(

            @PathVariable
            Long userId

    ){

        Long currentUserId =
                getCurrentUserId();

        Swipe swipe =
                swipeService
                        .createSwipe(

                                currentUserId,

                                userId,

                                SwipeType.LIKE

                        );

        return ResponseEntity.ok(
                map(
                        swipe
                )
        );

    }

    @PostMapping(
            "/pass/{userId}"
    )
    public ResponseEntity<SwipeResponseDTO>
    passUser(

            @PathVariable
            Long userId

    ){

        Long currentUserId =
                getCurrentUserId();

        Swipe swipe =
                swipeService
                        .createSwipe(

                                currentUserId,

                                userId,

                                SwipeType.PASS

                        );

        return ResponseEntity.ok(
                map(
                        swipe
                )
        );

    }

    @GetMapping(
            "/likes/me"
    )
    public ResponseEntity<List<SwipeResponseDTO>>
    myLikes(){

        List<SwipeResponseDTO> list =

                swipeService
                        .getMyLikes(
                                getCurrentUserId()
                        )

                        .stream()

                        .map(
                                this::map
                        )

                        .collect(
                                Collectors.toList()
                        );

        return ResponseEntity.ok(
                list
        );

    }

    @GetMapping(
            "/received"
    )
    public ResponseEntity<List<SwipeResponseDTO>>
    receivedLikes(){

        List<SwipeResponseDTO> list =

                swipeService
                        .getLikesReceived(
                                getCurrentUserId()
                        )

                        .stream()

                        .map(
                                this::map
                        )

                        .collect(
                                Collectors.toList()
                        );

        return ResponseEntity.ok(
                list
        );

    }

    @DeleteMapping(
            "/{userId}"
    )
    public ResponseEntity<Void>
    removeLike(

            @PathVariable
            Long userId

    ){

        swipeService.removeSwipe(

                getCurrentUserId(),

                userId

        );

        return ResponseEntity
                .noContent()
                .build();

    }

}