package com.example.serviceimpl;

import com.example.dto.response.ConversationListDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    // ================= USER =================

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public long getUnreadCount(Long conversationId, Long userId) {
        return messageRepository.countUnreadMessages(conversationId, userId);
    }

    // ================= SEND =================

    @Override
    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = getUser(senderId);
        User receiver = getUser(receiverId);
        return createTextMessage(sender, receiver, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content) {
        return sendMessageByEmail(senderEmail, receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(
            String senderEmail,
            Long receiverId,
            String content,
            Long replyToMessageId
    ) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        Message replyTo = null;
        if (replyToMessageId != null) {
            replyTo = messageRepository.findById(replyToMessageId)
                    .orElseThrow(() -> new RuntimeException("Reply message not found"));
        }

        return createTextMessage(sender, receiver, content, replyTo);
    }

    // ================= MEDIA =================

    @Override
    @Transactional
    public Message sendMediaMessage(
            String senderEmail,
            Long receiverId,
            String mediaUrl,
            String mediaType
    ) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        Conversation conversation = getOrCreateConversation(sender, receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setConversation(conversation);
        message.setMediaUrl(mediaUrl);
        message.setMediaType(mediaType);
        message.setMessageType("MEDIA");
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());

        // ✅ Avoid null content
        message.setContent(getMediaContent(mediaType));

        return messageRepository.save(message);
    }

    // ================= FORWARD =================

    @Override
    public Message forwardMessage(String senderEmail, Long messageId, Long receiverId) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        Message original = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        Conversation conversation = getOrCreateConversation(sender, receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setConversation(conversation);
        message.setContent(original.getContent());
        message.setMediaUrl(original.getMediaUrl());
        message.setMediaType(original.getMediaType());
        message.setMessageType("FORWARD");
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    // ================= GET =================

    @Override
    @Transactional
    public Page<Message> getMessages(Long userId, Long otherUserId, int page, int size) {

        User user1 = getUser(userId);
        User user2 = getUser(otherUserId);

        Conversation conversation = conversationRepository
                .findByUser1AndUser2(user1, user2)
                .or(() -> conversationRepository.findByUser1AndUser2(user2, user1))
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return messageRepository.findByConversation(conversation, pageable);
    }

    @Override
    public List<Message> getMessages(Long userId, Long otherUserId) {
        Conversation conversation = getConversation(userId, otherUserId);
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
    }

    @Override
    public List<Message> getMessagesByEmail(String email, Long otherUserId) {
        User user = getUserByEmail(email);
        return getMessages(user.getId(), otherUserId);
    }

    // ================= CONVERSATION =================

    @Override
    public List<ConversationListDTO> getUserConversations(Long userId) {
        return conversationRepository.getConversationList(userId); // ✅ FIXED
    }

    @Override
    public List<ConversationListDTO> getUserConversationsByEmail(String email) {
        User user = getUserByEmail(email);
        return conversationRepository.getConversationList(user.getId()); // ✅ FIXED
    }

    // ================= STATUS =================

    @Override
    @Transactional
    public void markAsSeen(Long conversationId, Long userId) {
        messageRepository.markAsSeen(
                conversationId,
                userId,
                MessageStatus.SEEN,
                LocalDateTime.now()
        );
    }

    @Override
    public void markAsDelivered(Long conversationId, Long userId) {
        List<Message> messages = messageRepository.findUndeliveredMessages(conversationId, userId);

        for (Message message : messages) {
            message.setStatus(MessageStatus.DELIVERED);
        }

        messageRepository.saveAll(messages);
    }

    // ================= DELETE =================

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepository.softDeleteMessage(messageId);
    }

    // ================= EDIT =================

    @Override
    public void editMessage(Long messageId, String content) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setContent(content);
        messageRepository.save(message);
    }

    // ================= REACTION =================

    @Override
    public void reactToMessage(Long messageId, String reaction) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setReaction(reaction);
        messageRepository.save(message);
    }

    @Override
    public void removeReaction(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setReaction(null);
        messageRepository.save(message);
    }

    // ================= BLOCK =================

    @Override
    public void blockUser(Long blockerId, Long blockedId) {}

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {}

    @Override
    public boolean isBlocked(Long user1, Long user2) {
        return false;
    }

    // ================= HELPER =================

    private Message createTextMessage(User sender, User receiver, String content, Message replyTo) {

        Conversation conversation = getOrCreateConversation(sender, receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setConversation(conversation);
        message.setContent(content);
        message.setMessageType("TEXT");
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());
        message.setReplyTo(replyTo);

        return messageRepository.save(message);
    }

    private String getMediaContent(String mediaType) {
        if ("IMAGE".equalsIgnoreCase(mediaType)) return "[Image]";
        if ("VIDEO".equalsIgnoreCase(mediaType)) return "[Video]";
        if ("AUDIO".equalsIgnoreCase(mediaType)) return "[Audio]";
        return "[Media]";
    }

    private Conversation getOrCreateConversation(User user1, User user2) {
        return conversationRepository
                .findByUser1AndUser2(user1, user2)
                .or(() -> conversationRepository.findByUser1AndUser2(user2, user1))
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(user1);
                    c.setUser2(user2);
                    return conversationRepository.save(c);
                });
    }

    private Conversation getConversation(Long user1Id, Long user2Id) {
        User user1 = getUser(user1Id);
        User user2 = getUser(user2Id);

        return conversationRepository
                .findByUser1AndUser2(user1, user2)
                .or(() -> conversationRepository.findByUser1AndUser2(user2, user1))
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }
}