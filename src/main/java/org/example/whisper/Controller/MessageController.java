package org.example.whisper.Controller;

import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.Message;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
import org.example.whisper.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService, UserRepository userRepository){
        this.messageService = messageService;

    }
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO dto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return messageService.sendMessage(dto, userDetails);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {
        return messageService.getChatMessages(chatId);
    }
}
