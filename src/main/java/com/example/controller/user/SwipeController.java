package com.example.controller.user;

import com.example.model.Swipe;
import com.example.model.SwipeType;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.SwipeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(
        "/api/swipes"
)

public class SwipeController {

    private final SwipeService swipeService;

    private final UserRepository userRepository;

    public SwipeController(

            SwipeService swipeService,

            UserRepository userRepository

    ){

        this.swipeService=
                swipeService;

        this.userRepository=
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

    @PostMapping(
            "/like/{userId}"
    )

    public ResponseEntity<Swipe>
    likeUser(

            @PathVariable
            Long userId

    ){

        Long currentUserId=
                getCurrentUserId();

        return ResponseEntity.ok(

                swipeService.createSwipe(

                        currentUserId,

                        userId,

                        SwipeType.LIKE

                )

        );

    }

    @PostMapping(
            "/pass/{userId}"
    )

    public ResponseEntity<Swipe>
    passUser(

            @PathVariable
            Long userId

    ){

        Long currentUserId=
                getCurrentUserId();

        return ResponseEntity.ok(

                swipeService.createSwipe(

                        currentUserId,

                        userId,

                        SwipeType.PASS

                )

        );

    }

    @GetMapping(
            "/likes/me"
    )

    public ResponseEntity<List<Swipe>>
    myLikes(){

        return ResponseEntity.ok(

                swipeService.getMyLikes(

                        getCurrentUserId()

                )

        );

    }

    @GetMapping(
            "/received"
    )

    public ResponseEntity<List<Swipe>>
    receivedLikes(){

        return ResponseEntity.ok(

                swipeService.getLikesReceived(

                        getCurrentUserId()

                )

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

        return ResponseEntity.noContent()

                .build();

    }

}