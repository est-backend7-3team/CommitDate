package est.commitdate.repository;

import est.commitdate.dto.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 채팅방의 메시지 조회
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(Long roomId);
}
