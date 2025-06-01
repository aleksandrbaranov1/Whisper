package org.example.whisper.Service;

import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.Message;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WebSocketMessageService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public WebSocketMessageService(UserRepository userRepository,
                                   ChatRepository chatRepository,
                                   MessageRepository messageRepository){
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
    public MessageDTO handleWebSocketMessage(MessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow();
        Chat chat = chatRepository.findById(dto.getChatId()).orElseThrow();

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setTimestamp(Instant.now());
        message.setChat(chat);
        message.setSender(sender);

        Message saved = messageRepository.save(message);
        return new MessageDTO(saved);
    }

}
