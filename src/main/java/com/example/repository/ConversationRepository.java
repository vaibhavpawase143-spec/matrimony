package com.example.repository;

import com.example.dto.response.ConversationListDTO;
import com.example.model.Conversation;
import com.example.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser1AndUser2(
            User user1,
            User user2
    );

    // ✅ FIXED: Removed 3 subqueries, using GROUP BY with aggregate functions
    // This executes in a single efficient query instead of N+3 queries
    @Query("""
        SELECT new com.example.dto.response.ConversationListDTO(
            c.id,
            CASE WHEN c.user1.id = :userId THEN c.user2.id ELSE c.user1.id END,
            CASE WHEN c.user1.id = :userId THEN c.user2.email ELSE c.user1.email END,
            (SELECT m.content FROM Message m 
             WHERE m.conversation.id = c.id 
             ORDER BY m.createdAt DESC LIMIT 1),
            MAX(m.createdAt),
            COUNT(CASE WHEN m.sender.id != :userId AND m.status = 'SENT' THEN 1 END),
            CASE WHEN c.user1.id = :userId THEN c.user2.isOnline ELSE c.user1.isOnline END,
            CASE WHEN c.user1.id = :userId THEN c.user2.lastSeen ELSE c.user1.lastSeen END
        )
        FROM Conversation c
        LEFT JOIN Message m ON m.conversation.id = c.id
        WHERE (c.user1.id = :userId OR c.user2.id = :userId) 
          AND c.isDeleted = FALSE
        GROUP BY c.id, c.user1.id, c.user1.email, c.user2.id, c.user2.email, 
                 c.user1.isOnline, c.user2.isOnline, c.user1.lastSeen, c.user2.lastSeen
        ORDER BY MAX(m.createdAt) DESC
    """)
    Page<ConversationListDTO> getConversationList(
            @Param("userId") Long userId,
            Pageable pageable
    );
    @Query("""
SELECT COUNT(c)
FROM Conversation c
WHERE c.user1.id = :userId
OR c.user2.id = :userId
""")
    long countConversations(
            @Param("userId") Long userId
    );

}