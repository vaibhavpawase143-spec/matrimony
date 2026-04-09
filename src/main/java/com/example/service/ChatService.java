package com.example.service;

import com.example.model.Message;
import com.example.dto.response.ConversationListDTO;
import com.example.model.User;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatService {

    // ================= SEND =================

    Message sendMessage(Long senderId, Long receiverId, String content);

    // ✅ NORMAL MESSAGE
    Message sendMessageByEmail(
            String senderEmail,
            Long receiverId,
            String content
    );

    // ✅ REPLY MESSAGE
    Message sendMessageByEmail(
            String senderEmail,
            Long receiverId,
            String content,
            Long replyToMessageId
    );

    // 🔥 MEDIA MESSAGE
    Message sendMediaMessage(
            String email,
            Long receiverId,
            String mediaUrl,
            String mediaType
    );

    // ================= GET MESSAGES =================

    // ✅ OLD (without pagination)
    List<Message> getMessages(Long userId, Long otherUserId);

    List<Message> getMessagesByEmail(String email, Long otherUserId);

    // 🔥 NEW (PAGINATION)
    Page<Message> getMessages(Long userId, Long otherUserId, int page, int size);

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

    // ================= 🛑 BLOCK =================

    void blockUser(Long blockerId, Long blockedId);

    void unblockUser(Long blockerId, Long blockedId);

    boolean isBlocked(Long user1, Long user2);

    // ================= 📤 FORWARD =================

    Message forwardMessage(String senderEmail, Long messageId, Long receiverId);

    // ================= ✏️ EDIT =================

    void editMessage(Long messageId, String content);
}