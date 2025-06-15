package org.example.whisper.Controller;

import org.example.whisper.DTO.ChatDTO;
import org.example.whisper.DTO.MarkAsReadRequest;
import org.example.whisper.Entity.Message;
import org.example.whisper.Service.MessageService;
import org.springframework.security.core.Authentication;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;
import org.example.whisper.Service.ChatService;
import org.example.whisper.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;

    public ChatController(ChatService chatService,
                          UserService userService,
                          MessageService messageService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping("/private")
    public ResponseEntity<ChatDTO> createPrivateChat(@RequestParam String user2Name, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.findByEmail(currentUsername); // тот, кто начинает чаь

        User user2 = userService.findByName(user2Name);
        Long user2Id = user2.getId();

        Chat chat = chatService.createPrivateChat(currentUser.getId(), user2Id);
        return ResponseEntity.ok(new ChatDTO(chat));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ChatDTO>> getMyChats(Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.findByEmail(currentUsername);

        List<Chat> chats = chatService.getChatsForUser(currentUser.getId());

        List<ChatDTO> chatDTOs = chats.stream()
                .map(chat -> {
                    Message lastMessage = messageService.getLastMessageForChat(chat.getId());
                    return new AbstractMap.SimpleEntry<>(chat, lastMessage);
                })
                .sorted((e1, e2) -> {
                    Message m1 = e1.getValue();
                    Message m2 = e2.getValue();

                    if (m1 == null && m2 == null) return 0;
                    if (m1 == null) return 1;
                    if (m2 == null) return -1;

                    return m2.getTimestamp().compareTo(m1.getTimestamp());
                })
                .map(entry -> new ChatDTO(entry.getKey(), entry.getValue()))
                .toList();

        return ResponseEntity.ok(chatDTOs);
    }


    @GetMapping("/searchUsers")
    public List<String> searchUsers(@RequestParam String name){
        return userService.searchUsers(name);
    }

    @DeleteMapping("/deleteChat/{chatId}")
    private ResponseEntity<Void> deleteChat(@PathVariable Long chatId, Authentication auth){
        User user = userService.findByEmail(auth.getName());
        chatService.deleteChat(chatId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
