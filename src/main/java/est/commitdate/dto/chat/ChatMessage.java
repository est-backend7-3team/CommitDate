package est.commitdate.dto.chat;

import est.commitdate.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "roomId")
    private ChatRoom chatRoom;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public static ChatMessage chat(ChatRoom chatRoom, ChatMessage chatMessage) {
        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.chatRoom = chatRoom;
        newChatMessage.sender = chatMessage.sender;
        newChatMessage.content = chatMessage.content;
        newChatMessage.timestamp = LocalDateTime.now();
        return newChatMessage;
    }


}