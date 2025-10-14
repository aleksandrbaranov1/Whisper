package org.example.whisper.Service;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatCacheService chatCacheService;

    public ChatService(UserRepository userRepository,
                       ChatRepository chatRepository,
                       MessageRepository messageRepository,
                       ChatCacheService chatCacheService) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.chatCacheService = chatCacheService;
    }

    public Chat createPrivateChat(Long user1Id, Long user2Id){
        if(user1Id.equals(user2Id)){
            throw new IllegalArgumentException("Нельзя создать чат с самим собой");
        }

        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id " + user1Id + " не найден"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id " + user2Id + " не найден"));

        List<Chat> allChats = chatRepository.findAll();
        for(Chat chat : allChats){
            Set<User> participants = chat.getParticipants();
            if(participants.size() == 2
                    && participants.contains(user1)
                    && participants.contains(user2)){
                chatCacheService.cacheChat(chat.getId(), chat); // сохраняем в кэш
                return chat;
            }
        }

        Chat chat = new Chat();
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        chat.setParticipants(users);

        Chat savedChat = chatRepository.save(chat);
        chatCacheService.cacheChat(savedChat.getId(), savedChat); // кэшируем новый чат
        return savedChat;
    }

    public Chat getChat(Long chatId) {
        Chat chat = chatCacheService.getChatFromCache(chatId);
        if (chat != null) return chat;

        chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат не найден"));
        chatCacheService.cacheChat(chatId, chat);
        return chat;
    }

    public List<Chat> getChatsForUser(Long userId){
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().stream()
                        .anyMatch(user ->user.getId().equals(userId)))
                .toList();
    }

    @Transactional
    public void deleteChat(Long chatId, Long userId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Чат " + chatId + " не найден"));

        boolean isParticipant = chat.getParticipants().stream()
                .anyMatch(user -> userId.equals(user.getId()));

        if(!isParticipant){
            throw new RuntimeException("Пользователь не имеет доступ к чату");
        }

        chatRepository.delete(chat);
        chatCacheService.evictChat(chatId); // удаляем из кэша
    }
}
