package org.example.whisper.DTO;

public class DeleteMessageDTO {
    private String action;
    private Long messageId;
    private Long chatId;

    public DeleteMessageDTO(){}

    public DeleteMessageDTO(Long messageId, Long chatId){
        this.action = "delete";
        this.messageId = messageId;
        this.chatId = chatId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
