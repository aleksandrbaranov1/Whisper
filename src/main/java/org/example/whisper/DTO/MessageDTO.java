package org.example.whisper.DTO;

import org.example.whisper.Entity.Message;

import java.time.Instant;
import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String content;
    private Instant timestamp;
    private Long senderId;
    private Long chatId;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.senderId = message.getSender().getId();
        this.chatId = message.getChat().getId();
    }
    public MessageDTO(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
