package est.commitdate.service;


import est.commitdate.dto.chat.ChatMessage;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.entity.ChatRoom;
import est.commitdate.exception.BoardNotFoundException;
import est.commitdate.exception.ChatRoomNotFoundException;
import est.commitdate.repository.ChatMessageRepository;
import est.commitdate.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;


    public void Chatting(ChatRoom chatRoom , ChatMessage message) {
        ChatMessage chat = ChatMessage.chat(chatRoom, message);
        chatMessageRepository.save(chat);
    }

    // 채팅방에 메시지 불러오기
    public List<ChatMessage> previousMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomRoomIdOrderByTimestampAsc(roomId);
    }

    // 채팅방 생성
    @Transactional
    public void ChatRoom(Long roomId) {
        if(chatRoomRepository.findById(roomId).isPresent()) {
            log.info("채팅방 이미 존재: " + roomId);
        } else {
            log.info("채팅방 생성됨" + roomId);
            ChatRoom chatRoom = ChatRoom.of(roomId);
            chatRoomRepository.save(ChatRoom.of(roomId));
        }
    }

    // 생성된 채팅방 찾기

    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(
                        ()-> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다.")
                );
    }

}
