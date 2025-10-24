package org.example.whisper.Service;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.ChatRepository;
import org.example.whisper.Repository.MessageRepository;
import org.example.whisper.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatCacheService chatCacheService;

    @InjectMocks
    private ChatService chatService;

    @Test
    public void testCreatePrivateChat_Success(){
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Vasya");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Petya");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        when(chatRepository.findAll()).thenReturn(Collections.emptyList());
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chat chat = chatService.createPrivateChat(1L, 2L);

        assertNotNull(chat);
        assertTrue(chat.getParticipants().contains(user1));
        assertTrue(chat.getParticipants().contains(user2));
        verify(chatRepository, times(1)).save(any(Chat.class));
    }
}
