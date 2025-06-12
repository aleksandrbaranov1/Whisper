package org.example.whisper.Service;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.Message;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> sendMessage(MessageDTO dto, UserDetails userDetails){
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
    public ResponseEntity<?> getChatMessages( Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        List<MessageDTO> result = messages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    public Message getLastMessageForChat(Long chatId){
        return messageRepository.findTopByChatIdOrderByTimestampDesc(chatId)
                .orElse(null);
    }
    @Transactional
    public void markMessagesAsRead(Long userId, List<Long> messageIds){
        List<Message> messages = messageRepository.findAllById(messageIds);
        System.out.println("Messages found: " + messages.size());
        for (Message msg : messages) {
            System.out.println("Message ID: " + msg.getId() +
                    ", Sender ID: " + msg.getSender().getId() +
                    ", Current User ID: " + userId);

            if (!msg.getSender().getId().equals(userId)) {
                msg.setRead(true);
                System.out.println("Marked message as read");
            } else {
                System.out.println("Skipped own message");
            }
        }

        messageRepository.saveAll(messages);
        messageRepository.flush();


    }
    @Transactional
    public void deleteMessage(Long chatId, Long messageId, Authentication auth){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат " + chatId +"не найден"));
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        boolean isParticipant = chat.getParticipants().stream()
                .anyMatch(currentUser -> user.getId().equals(currentUser.getId()));
        if(!isParticipant){
            throw new RuntimeException("Пользователь не имеет доступ к чату");
        }
        messageRepository.deleteById(messageId);
    }
    public ResponseEntity<?> editMessage(Long messageId, String newContent, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Message message = messageRepository.findMessageById(messageId).orElseThrow(
                () -> new RuntimeException("Сообщение не найдено"));
        Chat chat = chatRepository.findById(message.getChat().getId())
                .orElseThrow(() ->new RuntimeException("Чат" + message.getChat().getId() + "не найден"));
        boolean isParticipant = chat.getParticipants().stream()
                        .anyMatch(currentUser -> user.getId().equals(currentUser.getId()));
        if(!isParticipant){
            throw new RuntimeException("Пользователь не имеет доступ к чату");
        }
        message.setContent(newContent);
        messageRepository.save(message);
        return ResponseEntity.ok(new MessageDTO(message));
    }
}
