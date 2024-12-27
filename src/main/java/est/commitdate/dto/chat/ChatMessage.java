package est.commitdate.dto.chat;

import est.commitdate.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String content;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private ChatRoom chatRoom;

    private LocalDateTime timestamp;

    public static ChatMessage chat(ChatRoom chatRoom, ChatMessage chatMessage) {
        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.chatRoom = chatRoom;
        newChatMessage.sender = chatMessage.sender;
        newChatMessage.content = chatMessage.content;
        newChatMessage.timestamp = chatMessage.timestamp;
        return newChatMessage;
    }

}