package com.example.service;

import com.example.model.Message;
import com.example.dto.response.ConversationListDTO;
import com.example.model.User;

import java.util.List;

public interface ChatService {

    // ================= SEND =================

    Message sendMessage(Long senderId, Long receiverId, String content);

    Message sendMessageByEmail(String email, Long receiverId, String content);

    // 🔥 MEDIA MESSAGE
    Message sendMediaMessage(String email, Long receiverId, String mediaUrl, String mediaType);


    // ================= GET MESSAGES =================

    List<Message> getMessages(Long userId, Long otherUserId);

    List<Message> getMessagesByEmail(String email, Long otherUserId);


    // ================= CONVERSATION =================

    List<ConversationListDTO> getUserConversations(Long userId);

    List<ConversationListDTO> getUserConversationsByEmail(String email);


    // ================= MESSAGE STATUS =================

    void markAsDelivered(Long conversationId, Long userId);

    void markAsSeen(Long conversationId, Long userId);


    // ================= USER =================

    User getUser(Long receiverId);

    User getUserByEmail(String email);


    // ================= UNREAD =================

    long getUnreadCount(Long conversationId, Long userId);


    // ================= DELETE =================

    void deleteMessage(Long messageId);


    // ================= ❤️ REACTION =================

    void reactToMessage(Long messageId, String reaction);

    void removeReaction(Long messageId);


    // ================= 🛑 BLOCK (🔥 THIS WAS MISSING) =================

    void blockUser(Long blockerId, Long blockedId);

    void unblockUser(Long blockerId, Long blockedId);

    boolean isBlocked(Long user1, Long user2);
}