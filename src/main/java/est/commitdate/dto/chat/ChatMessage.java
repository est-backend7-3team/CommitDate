package est.commitdate.dto.chat;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;
    private String sender;
    private String content;

    private LocalDateTime timestamp; // 추가: 메시지 생성 시간을 저장하는 필드


}