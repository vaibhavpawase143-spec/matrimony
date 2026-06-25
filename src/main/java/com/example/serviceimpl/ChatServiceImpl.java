package com.example.serviceimpl;

import com.example.dto.response.ConversationListDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;
import com.example.service.NotificationService;
import com.example.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserBlockService userBlockService; // ✅ FIXED
    private final NotificationService notificationService;
    private final MatchRepository matchRepository;
    private final DeletedMessageRepository deletedMessageRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    // ================= USER =================

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public long getUnreadCount(Long conversationId, Long userId) {
        return messageRepository.countUnreadMessages(conversationId, userId);
    }

    // ================= VALIDATION =================

    private void validateMatch(Long u1, Long u2) {

        Long smaller = Math.min(u1, u2);
        Long larger = Math.max(u1, u2);

        boolean exists = matchRepository
                .existsByUser1_IdAndUser2_Id(smaller, larger);

        if (!exists) {
            throw new RuntimeException("❌ Chat not allowed without match");
        }
    }

    // ✅ FINAL FIX (NO ERROR)
    private void validateNotBlocked(Long u1, Long u2) {

        if (userBlockService.isBlocked(u1, u2)) {
            throw new RuntimeException("User is blocked");
        }
    }

    // ================= CONVERSATION =================

    private Conversation getOrCreateConversation(User u1, User u2) {

        if (u1.getId().equals(u2.getId())) {
            throw new RuntimeException("Cannot create conversation with yourself");
        }

        return conversationRepository
                .findByUser1AndUser2(u1, u2)
                .or(() -> conversationRepository.findByUser1AndUser2(u2, u1))
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(u1);
                    c.setUser2(u2);
                    return conversationRepository.save(c);
                });
    }

    private Conversation getConversation(User u1, User u2) {
        return conversationRepository
                .findByUser1AndUser2(u1, u2)
                .or(() -> conversationRepository.findByUser1AndUser2(u2, u1))
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    // ================= CREATE MESSAGE =================

    private Message createTextMessage(User sender,
                                      User receiver,
                                      String content,
                                      Message replyTo) {

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();

        m.setSender(sender);
        m.setReceiver(receiver);
        m.setConversation(c);

        // TEXT MESSAGE
        m.setContent(encrypt(content));
        m.setMessageType("TEXT");

        // Reply
        if (replyTo != null) {
            m.setReplyTo(replyTo);
        }

        m.setCreatedAt(LocalDateTime.now());

        Message saved = messageRepository.save(m);

        notificationService.create(
                sender.getId(),
                receiver.getId(),
                NotificationType.MESSAGE
        );

        return saved;
    }

    // ================= SEND =================

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        return sendMessageByEmail(getUser(senderId).getEmail(), receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content) {
        return sendMessageByEmail(senderEmail, receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content, Long replyToId) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

// Premium Validation
        validatePremium(sender.getId());


// Existing validations
        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());
        Message replyTo = null;
        if (replyToId != null) {
            replyTo = messageRepository.findById(replyToId)
                    .orElseThrow(() -> new RuntimeException("Reply not found"));
        }

        Message saved = createTextMessage(sender, receiver, content, replyTo);

        saved.setContent(decrypt(saved.getContent()));

// 🔥 Send message instantly to receiver
        messagingTemplate.convertAndSendToUser(
                receiver.getEmail(),
                "/queue/messages",
                saved
        );

// 🔥 Also send to sender (so sender updates instantly too)
        messagingTemplate.convertAndSendToUser(
                sender.getEmail(),
                "/queue/messages",
                saved
        );

        return saved;
    }

    // ================= MEDIA =================

    @Override
    public Message sendMediaMessage(String email, Long receiverId, String mediaUrl, String mediaType) {

        User sender = getUserByEmail(email);
        User receiver = getUser(receiverId);

        validatePremium(sender.getId());


        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());

        Message m = new Message();
        m.setSender(sender);
        m.setReceiver(receiver);
        Conversation conversation = getOrCreateConversation(sender, receiver);
        m.setConversation(conversation);
        m.setMediaUrl(mediaUrl);
        m.setMediaType(mediaType);
        m.setMessageType("MEDIA");
        m.setCreatedAt(LocalDateTime.now());
        m.setContent(encrypt(getMediaContent(mediaType)));

        Message saved = messageRepository.save(m);

        saved.setContent(decrypt(saved.getContent()));

        return saved;
    }

    // ================= GET =================

    @Override
    public List<Message> getMessages(
            Long userId,
            Long otherUserId
    ) {

        User u1 = getUser(userId);

        User u2 = getUser(otherUserId);

        Conversation conversation =

                conversationRepository

                        .findByUser1AndUser2(
                                u1,
                                u2
                        )

                        .or(() ->

                                conversationRepository

                                        .findByUser1AndUser2(
                                                u2,
                                                u1
                                        )

                        )

                        .orElse(null);

        if(conversation==null){

            return List.of();

        }

        List<Message> list =

                messageRepository

                        .findByConversationOrderByCreatedAtAsc(
                                conversation
                        );

        Set<Long> deletedIds =

                deletedMessageRepository

                        .findByUserId(userId)

                        .stream()

                        .map(
                                DeletedMessage::getMessageId
                        )

                        .collect(Collectors.toSet());

        list =

                list.stream()

                        .filter(
                                m -> !deletedIds.contains(
                                        m.getId()
                                )
                        )

                        .collect(Collectors.toList());

        list.forEach(m -> {

            System.out.println(

                    "DB VALUE = "

                            + m.getContent()

            );

            String decrypted =

                    decrypt(

                            m.getContent()

                    );

            System.out.println(

                    "DECRYPTED = "

                            + decrypted

            );

            m.setContent(

                    decrypted

            );

        });
        return list;

    }

    @Override
    public List<Message> getMessagesForChat(

            Long senderId,

            Long receiverId

    ){

        return getMessages(

                senderId,

                receiverId

        );

    }

    @Override
    public List<Message> getMessagesByEmail(String email, Long otherUserId) {
        return getMessages(getUserByEmail(email).getId(), otherUserId);
    }

    @Override
    public Page<Message> getMessages(Long userId, Long otherUserId, int page, int size) {

        Conversation c = getConversation(getUser(userId), getUser(otherUserId));

        Page<Message> p = messageRepository.findByConversation(
                c, PageRequest.of(page, size, Sort.by("createdAt").ascending())
        );

        p.getContent().forEach(m -> m.setContent(decrypt(m.getContent())));

        return p;
    }

    // ================= STATUS =================

    @Override
    public void markAsDelivered(
            Long conversationId,
            Long userId
    ) {

        List<Message> messages =
                messageRepository
                        .findUndeliveredMessages(
                                conversationId,
                                userId
                        );

        messages.forEach(
                m -> m.setStatus(
                        MessageStatus.DELIVERED
                )
        );

        messageRepository.saveAll(
                messages
        );
    }

    @Override
    public void markAsSeen(Long conversationId, Long userId) {
        messageRepository.markAsSeen(conversationId, userId, MessageStatus.SEEN, LocalDateTime.now());
    }

    // ================= CONVERSATION =================

    @Override
    public List<ConversationListDTO> getUserConversations(Long userId) {

        List<ConversationListDTO> list =
                conversationRepository.getConversationList(userId);

        list.forEach(dto -> {

            if(dto.getLastSeen() != null){

                boolean online =
                        dto.getLastSeen()
                                .isAfter(
                                        LocalDateTime.now()
                                                .minusSeconds(20)
                                );

                dto.setIsOnline(online);

            }else{

                dto.setIsOnline(false);

            }

        });

        return list;
    }

    @Override
    public List<ConversationListDTO> getUserConversationsByEmail(String email) {

        List<ConversationListDTO> list =
                conversationRepository.getConversationList(
                        getUserByEmail(email).getId()
                );

        list.forEach(dto -> {

            if(dto.getLastSeen() != null){

                boolean online =

                        dto.getLastSeen()

                                .isAfter(

                                        LocalDateTime.now()

                                                .minusMinutes(2)

                                );

                dto.setIsOnline(online);

            }else{

                dto.setIsOnline(false);

            }

        });

        return list;
    }

    // ================= REACTION =================

    @Override
    public void reactToMessage(Long id, String reaction) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        m.setReaction(reaction);
        messageRepository.save(m);
    }

    @Override
    public void removeReaction(Long messageId){}

    @Override
    @Transactional
    public void pinMessage(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new RuntimeException("Message not found"));

        message.setPinned(true);

        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void unpinMessage(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new RuntimeException("Message not found"));

        message.setPinned(false);

        messageRepository.save(message);
    }
    @Override
    @Transactional
    public void starMessage(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new RuntimeException("Message not found"));

        message.setStarred(true);

        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void unstarMessage(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new RuntimeException("Message not found"));

        message.setStarred(false);

        messageRepository.save(message);
    }

    // ================= BLOCK =================

    @Override
    public void blockUser(Long blockerId, Long blockedId) {
        userBlockService.blockUser(blockerId, blockedId);
    }

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {
        userBlockService.unblockUser(blockerId, blockedId);
    }

    @Override
    public boolean isBlocked(Long user1, Long user2) {
        return userBlockService.isBlocked(user1, user2);
    }

    @Override
    public Message forwardMessage(String senderEmail, Long messageId, Long receiverId) {

        // 🔍 Get users
        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        // 🔒 Validation
        // 🔒 Validation
        validatePremium(sender.getId());
     //   validatePremium(receiver.getId());

        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());
        // 📩 Get original message
        Message original = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Original message not found"));

        // 💬 Get or create conversation
        Conversation conversation = getOrCreateConversation(sender, receiver);

        // 🆕 Create new message
        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setConversation(conversation);

        // 🔁 Copy content (already encrypted in DB)
        newMessage.setContent(original.getContent());

        // 📸 Copy media if exists
        newMessage.setMediaUrl(original.getMediaUrl());
        newMessage.setMediaType(original.getMediaType());

        newMessage.setMessageType("FORWARD");
        newMessage.setCreatedAt(LocalDateTime.now());

        // 💾 Save
        Message saved = messageRepository.save(newMessage);
        notificationService.create(
                sender.getId(),
                receiver.getId(),
                NotificationType.MESSAGE
        );
        // 🔓 Decrypt before sending response
        saved.setContent(decrypt(saved.getContent()));

        return saved;
    }

    // ================= DELETE =================

    @Override
    public void deleteMessage(Long id) {
        messageRepository.softDeleteMessage(id);
    }


    @Override
    @Transactional
    public void deleteForMe(
            Long messageId,
            Long userId
    ) {

        Message message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow();

        String users =
                message.getDeletedForUsers();

        if(users == null){
            users = "";
        }

        if(!users.contains(userId + ",")){
            users += userId + ",";
        }

        message.setDeletedForUsers(users);

        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteForEveryone(
            Long messageId,
            Long userId
    ) {

        Message message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow();

        if(
                !message.getSender()
                        .getId()
                        .equals(userId)
        ){
            throw new RuntimeException(
                    "Only sender can delete for everyone"
            );
        }

        message.setDeletedForEveryone(true);
        message.setDeletedAt(LocalDateTime.now());

        messageRepository.save(message);
    }

    // ================= EDIT =================

    @Override
    public void editMessage(Long id, String content) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        m.setContent(encrypt(content));
        messageRepository.save(m);
    }

    // ================= ENCRYPT =================

    private String encrypt(String data){

        return data;

    }

    private String decrypt(String data){

        return data;

    }

    private String getMediaContent(String type) {
        return switch (type.toUpperCase()) {
            case "IMAGE" -> "[Image]";
            case "VIDEO" -> "[Video]";
            case "AUDIO" -> "[Audio]";
            default -> "[Media]";
        };
    }
    private void validatePremium(Long userId) {

        boolean premium =
                userSubscriptionRepository
                        .findFirstByUser_IdAndIsActiveTrueAndStatusAndEndDateAfter(
                                userId,
                                "ACTIVE",
                                LocalDateTime.now()
                        )
                        .isPresent();

        if (!premium) {
            throw new RuntimeException(
                    "Chat is available only for Premium members."
            );
        }
    }
}