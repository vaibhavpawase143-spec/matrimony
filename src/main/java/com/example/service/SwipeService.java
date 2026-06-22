package com.example.service;

import com.example.model.Swipe;
import com.example.model.SwipeType;

import java.util.List;
import java.util.Optional;

public interface SwipeService {

    Swipe createSwipe(

            Long fromUserId,

            Long toUserId,

            SwipeType type

    );

    Optional<Swipe> getSwipe(

            Long fromUserId,

            Long toUserId

    );

    List<Swipe> getMyLikes(

            Long userId

    );

    List<Swipe> getLikesReceived(

            Long userId

    );

    void removeSwipe(

            Long fromUserId,

            Long toUserId

    );

}