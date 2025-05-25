package org.example.whisper.Service;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public ChatService(UserRepository userRepository, ChatRepository chatRepository){
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    public Chat createPrivateChat(Long user1Id, Long user2Id){
        if(user1Id.equals(user2Id)){
            throw new IllegalArgumentException("Нельзя создать чат с самим собой");
        }

        User user1 = userRepository.findById(user1Id).
                orElseThrow(() -> new NoSuchElementException
                        ("Пользователь с id " + user1Id + " не найден"));
        User user2 = userRepository.findById(user2Id).
                orElseThrow(() -> new NoSuchElementException
                        ("Пользователь с id " + user2Id + " не найден"));

        List<Chat> allChats = chatRepository.findAll();
        for(Chat chat : allChats){
            Set<User> participants = chat.getParticipants();
            if(participants.size() == 2
                    && participants.contains(user1)
                    && participants.contains(user2)){
                return chat;
            }
        }

        Chat chat = new Chat();
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        chat.setParticipants(users);

        return chatRepository.save(chat);
    }

    public List<Chat> getChatsForUser(Long userId){
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().stream()
                        .anyMatch(user ->user.getId().equals(userId))).toList();
    }
}
