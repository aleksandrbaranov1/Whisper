package org.example.whisper.DTO;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.Message;
import org.example.whisper.Entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ChatDTO {
    private Long id;
    private Set<UserDTO> participants;
    private MessageDTO lastMessage;

    public ChatDTO(Chat chat, Message lastMessage){
        this.id = chat.getId();
        this.participants = chat.getParticipants()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toSet());
        this.lastMessage = lastMessage != null ? new MessageDTO(lastMessage) : null; // 🆕
    }
    public ChatDTO(Chat chat) {
        this(chat, null);
    }


    public MessageDTO getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDTO lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserDTO> participants) {
        this.participants = participants;
    }
}
