package com.example.serviceimpl;

import com.example.dto.response.DashboardSummaryDTO;
import com.example.model.SwipeType;
import com.example.repository.*;
import com.example.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.service.MatchService;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final MatchRepository matchRepository;
    private final InterestRepository interestRepository;
    private final ShortlistRepository shortlistRepository;
    private final ProfileVisitorRepository visitorRepository;
    private final SwipeRepository swipeRepository;
    private final ConversationRepository conversationRepository;
    private final MatchService matchService;

    @Override
    @Cacheable(
            value = "dashboardSummary",
            key = "#userId"
    )
    public DashboardSummaryDTO getSummary(Long userId) {

        return DashboardSummaryDTO.builder()

                .totalMatches(0)

                .interestsSent(
                        interestRepository.countBySender_IdAndIsActiveTrue(userId)
                )

                .interestsReceived(
                        interestRepository.countByReceiver_IdAndIsActiveTrue(userId)
                )

                .shortlists(
                        shortlistRepository.countActiveShortlistsByUser(userId)
                )

                .profileViews(
                        visitorRepository.countByVisitedUser_Id(userId)
                )

                .likesReceived(
                        swipeRepository.countByToUser_IdAndType(
                                userId,
                                SwipeType.LIKE
                        )
                )

                .messages(
                        conversationRepository.countConversations(userId)
                )

                .build();
    }
}