package com.example.controller.user;

import com.example.dto.request.ChatMessageDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ConversationListDTO;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // ================= SEND MESSAGE =================
    @PostMapping("/send")
    public ApiResponse<Message> sendMessage(
            @RequestParam Long receiverId,
            @RequestParam String content,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Message message = chatService.sendMessageByEmail(
                senderEmail,
                receiverId,
                content
        );

        String receiverEmail = message.getReceiver().getEmail();

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .sender(senderEmail)
                .receiver(receiverEmail)
                .content(content)
                .type("CHAT")
                .conversationId(message.getConversation().getId())
                .messageId(message.getId())
                .status("SENT")
                .build();

        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", dto);
        messagingTemplate.convertAndSendToUser(senderEmail, "/queue/messages", dto);

        // DELIVERED
        ChatMessageDTO deliveredDto = ChatMessageDTO.builder()
                .sender(receiverEmail)
                .receiver(senderEmail)
                .type("DELIVERED")
                .conversationId(message.getConversation().getId())
                .messageId(message.getId())
                .status("DELIVERED")
                .build();

        messagingTemplate.convertAndSendToUser(senderEmail, "/queue/messages", deliveredDto);

        // UNREAD
        ChatMessageDTO unreadDto = ChatMessageDTO.builder()
                .type("UNREAD")
                .conversationId(message.getConversation().getId())
                .build();

        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", unreadDto);

        // PUSH
        ChatMessageDTO notification = ChatMessageDTO.builder()
                .type("NOTIFICATION")
                .sender(senderEmail)
                .receiver(receiverEmail)
                .content(content)
                .build();

        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/notification", notification);

        // UPDATE LIST
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + senderEmail,
                chatService.getUserConversationsByEmail(senderEmail)
        );

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + receiverEmail,
                chatService.getUserConversationsByEmail(receiverEmail)
        );

        return ApiResponse.<Message>builder()
                .success(true)
                .message("Message sent")
                .data(message)
                .build();
    }

    // ================= 📸 SEND MEDIA =================
    @PostMapping("/send-media")
    public void sendMedia(
            @RequestParam Long receiverId,
            @RequestParam String mediaUrl,
            @RequestParam String mediaType,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Message message = chatService.sendMediaMessage(
                senderEmail,
                receiverId,
                mediaUrl,
                mediaType
        );

        String receiverEmail = message.getReceiver().getEmail();

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .type("MEDIA")
                .sender(senderEmail)
                .receiver(receiverEmail)
                .conversationId(message.getConversation().getId())
                .messageId(message.getId())
                .build();

        messagingTemplate.convertAndSendToUser(receiverEmail, "/queue/messages", dto);
    }

    // ================= ❤️ REACTION =================
    @PutMapping("/reaction")
    public void react(
            @RequestParam Long messageId,
            @RequestParam String reaction
    ) {

        chatService.reactToMessage(messageId, reaction);

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .type("REACTION")
                .messageId(messageId)
                .status(reaction)
                .build();

        messagingTemplate.convertAndSend("/topic/reaction", dto);
    }

    // ================= ❌ REMOVE REACTION =================
    @DeleteMapping("/reaction/{messageId}")
    public void removeReaction(@PathVariable Long messageId) {

        chatService.removeReaction(messageId);

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .type("REMOVE_REACTION")
                .messageId(messageId)
                .build();

        messagingTemplate.convertAndSend("/topic/reaction", dto);
    }

    // ================= 🗑 DELETE MESSAGE =================
    @DeleteMapping("/message/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {

        chatService.deleteMessage(messageId);

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .type("DELETE")
                .messageId(messageId)
                .build();

        messagingTemplate.convertAndSend("/topic/delete", dto);
    }

    // ================= ✍️ TYPING =================
    @PostMapping("/typing")
    public void typing(
            @RequestParam Long receiverId,
            @RequestParam String type,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        User receiver = chatService.getUser(receiverId);

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .sender(senderEmail)
                .receiver(receiver.getEmail())
                .type(type)
                .build();

        messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/messages", dto);
    }

    // ================= 🟢 ONLINE =================
    @PostMapping("/online")
    public void broadcastOnline(Principal principal) {

        String email = principal.getName();

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .sender(email)
                .type("ONLINE")
                .build();

        messagingTemplate.convertAndSend("/topic/status", dto);
    }

    // ================= 📩 GET MESSAGES =================
    @GetMapping("/messages")
    public ApiResponse<List<Message>> getMessages(
            @RequestParam Long otherUserId,
            Principal principal
    ) {

        String email = principal.getName();

        return ApiResponse.<List<Message>>builder()
                .success(true)
                .message("Messages fetched")
                .data(chatService.getMessagesByEmail(email, otherUserId))
                .build();
    }

    // ================= 💬 GET CONVERSATIONS =================
    @GetMapping("/conversations")
    public ApiResponse<List<ConversationListDTO>> getConversations(
            Principal principal
    ) {

        String email = principal.getName();

        return ApiResponse.<List<ConversationListDTO>>builder()
                .success(true)
                .message("Conversations fetched")
                .data(chatService.getUserConversationsByEmail(email))
                .build();
    }

    // ================= ✔✔ SEEN =================
    @PutMapping("/seen/{conversationId}")
    public void markAsSeen(
            @PathVariable Long conversationId,
            Principal principal
    ) {

        String email = principal.getName();

        User user = chatService.getUserByEmail(email);

        chatService.markAsSeen(conversationId, user.getId());

        ChatMessageDTO dto = ChatMessageDTO.builder()
                .type("SEEN")
                .sender(email)
                .conversationId(conversationId)
                .status("SEEN")
                .build();

        messagingTemplate.convertAndSend("/topic/seen/" + conversationId, dto);
    }
    @PostMapping("/block/{userId}")
    public void blockUser(@PathVariable Long userId, Principal principal) {

        User me = chatService.getUserByEmail(principal.getName());

        chatService.blockUser(me.getId(), userId);
    }
}