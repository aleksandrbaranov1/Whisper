package org.example.whisper.DTO;

import java.util.List;

public class MarkAsReadRequest {
    private List<Long> messageIds;

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }
}
