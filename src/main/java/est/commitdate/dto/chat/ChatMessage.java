package est.commitdate.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ChatMessage {
    private String sender;
    private String content;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

}