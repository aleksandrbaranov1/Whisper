package org.example.whisper.DTO;

import org.example.whisper.Entity.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {
    private Long id;
    private String content;
    private Instant timestamp;
    private Long senderId;
    private Long chatId;
    private boolean isRead;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.senderId = message.getSender().getId();
        this.chatId = message.getChat().getId();
        this.isRead = message.isRead();
    }
    public MessageDTO(){

    }

    @JsonProperty("isRead")
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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
