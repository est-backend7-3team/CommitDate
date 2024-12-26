package est.commitdate.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String sender;  // 메시지 보낸 사람
    private String content; // 메시지 내용
    private String roomId;  // 채팅방 ID
    private LocalDateTime sendTime; // 메시지 보낸 시간
}