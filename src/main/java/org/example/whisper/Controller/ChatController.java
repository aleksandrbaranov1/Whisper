package org.example.whisper.Controller;

import org.example.whisper.DTO.ChatDTO;
import org.springframework.security.core.Authentication;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;
import org.example.whisper.Service.ChatService;
import org.example.whisper.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService; // нужен для извлечения User по имени (или email)

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/private")
    public ResponseEntity<ChatDTO> createPrivateChat(@RequestParam Long user2Id, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.findByEmail(currentUsername); // или findByEmail

        Chat chat = chatService.createPrivateChat(currentUser.getId(), user2Id);
        return ResponseEntity.ok(new ChatDTO(chat));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ChatDTO>> getMyChats(Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.findByEmail(currentUsername); // или findByEmail

        List<Chat> chats = chatService.getChatsForUser(currentUser.getId());
        List<ChatDTO> chatDTOs = chats.stream()
                .map(ChatDTO :: new)
                .toList();
        return ResponseEntity.ok(chatDTOs);
    }
}
