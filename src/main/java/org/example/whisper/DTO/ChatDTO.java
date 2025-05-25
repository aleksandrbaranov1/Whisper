package org.example.whisper.DTO;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class ChatDTO {
    private Long id;
    private Set<UserDTO> participants;

    public ChatDTO(Chat chat){
        this.id = chat.getId();
        this.participants = chat.getParticipants()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toSet());
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
