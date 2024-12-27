package est.commitdate.entity;

import est.commitdate.dto.chat.ChatMessage;
import est.commitdate.dto.swipe.ChooseDto;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class ChatRoom {
    @Id
    private Long roomId;

    private Integer status;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom of(Long roomId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = roomId;
        chatRoom.status = 1;
        return chatRoom;
    }

}
