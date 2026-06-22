package com.example.serviceimpl;

import com.example.model.NotificationType;
import com.example.model.Swipe;
import com.example.model.SwipeType;
import com.example.model.User;
import com.example.repository.SwipeRepository;
import com.example.repository.UserRepository;
import com.example.service.NotificationService;
import com.example.service.SwipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class SwipeServiceImpl
        implements SwipeService {

    private final SwipeRepository swipeRepository;

    private final UserRepository userRepository;

    private final NotificationService notificationService;

    public SwipeServiceImpl(

            SwipeRepository swipeRepository,

            UserRepository userRepository,

            NotificationService notificationService

    ){

        this.swipeRepository =
                swipeRepository;

        this.userRepository =
                userRepository;

        this.notificationService =
                notificationService;

    }

    @Override
    public Swipe createSwipe(

            Long fromUserId,

            Long toUserId,

            SwipeType type

    ){

        if(
                fromUserId.equals(
                        toUserId
                )
        ){

            throw new RuntimeException(
                    "You cannot swipe yourself"
            );

        }

        User fromUser =
                userRepository
                        .findById(fromUserId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                ));

        User toUser =
                userRepository
                        .findById(toUserId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                ));

        Optional<Swipe> existing =
                swipeRepository
                        .findByFromUserAndToUser(
                                fromUser,
                                toUser
                        );

        // Existing swipe update
        if(
                existing.isPresent()
        ){

            Swipe swipe =
                    existing.get();

            swipe.setType(
                    type
            );

            Swipe updated =
                    swipeRepository.save(
                            swipe
                    );

            if(type == SwipeType.LIKE){

                notificationService.create(

                        fromUserId,

                        toUserId,

                        NotificationType.LIKE

                );

            }

            return updated;

        }

        // New swipe
        Swipe swipe =
                new Swipe();

        swipe.setFromUser(
                fromUser
        );

        swipe.setToUser(
                toUser
        );

        swipe.setType(
                type
        );

        Swipe saved =
                swipeRepository.save(
                        swipe
                );

        if(type == SwipeType.LIKE){

            notificationService.create(

                    fromUserId,

                    toUserId,

                    NotificationType.LIKE

            );

        }

        return saved;

    }

    @Override
    public Optional<Swipe>
    getSwipe(

            Long fromUserId,

            Long toUserId

    ){

        User fromUser =
                userRepository
                        .findById(
                                fromUserId
                        )
                        .orElseThrow();

        User toUser =
                userRepository
                        .findById(
                                toUserId
                        )
                        .orElseThrow();

        return swipeRepository
                .findByFromUserAndToUser(
                        fromUser,
                        toUser
                );

    }

    @Override
    public List<Swipe>
    getMyLikes(
            Long userId
    ){

        User user =
                userRepository
                        .findById(
                                userId
                        )
                        .orElseThrow();

        return swipeRepository
                .findByFromUserAndType(

                        user,

                        SwipeType.LIKE

                );

    }

    @Override
    public List<Swipe>
    getLikesReceived(
            Long userId
    ){

        User user =
                userRepository
                        .findById(
                                userId
                        )
                        .orElseThrow();

        return swipeRepository
                .findByToUserAndType(

                        user,

                        SwipeType.LIKE

                );

    }

    @Override
    public void removeSwipe(

            Long fromUserId,

            Long toUserId

    ){

        User from =
                userRepository
                        .findById(
                                fromUserId
                        )
                        .orElseThrow();

        User to =
                userRepository
                        .findById(
                                toUserId
                        )
                        .orElseThrow();

        swipeRepository
                .findByFromUserAndToUser(
                        from,
                        to
                )

                .ifPresent(

                        swipe ->

                                swipeRepository.delete(
                                        swipe
                                )

                );

    }

}
