package org.example.whisper.Controller;

import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.Message;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
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

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO dto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        User sender = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Chat chat = chatRepository.findById(dto.getChatId()).orElseThrow();

        Message message = new Message();
        message.setContent(dto.getContent());
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        message.setTimestamp(instant);

        message.setChat(chat);
        message.setSender(sender);

        Message saved = messageRepository.save(message);
        return ResponseEntity.ok(new MessageDTO(saved));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        List<MessageDTO> result = messages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
