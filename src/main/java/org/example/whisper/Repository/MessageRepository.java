package org.example.whisper.Repository;

import org.example.whisper.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(Long chatId);
    Optional<Message> findTopByChatIdOrderByTimestampDesc(Long chatId);
    Void deleteMessageById(Long messageId);

}
