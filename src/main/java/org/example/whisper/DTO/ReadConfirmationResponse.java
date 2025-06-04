package org.example.whisper.DTO;

import java.util.List;

public class ReadConfirmationResponse {
    private List<Long> messageIds;

    public ReadConfirmationResponse() {
    }

    public ReadConfirmationResponse(List<Long> messageIds) {
        this.messageIds = messageIds;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }
}
