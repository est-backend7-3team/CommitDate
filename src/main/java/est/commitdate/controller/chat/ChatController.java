package est.commitdate.controller.chat;




import est.commitdate.dto.chat.ChatMessage;

import est.commitdate.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;  // 메시지 저장소를 추

    @GetMapping("/chatting/{roomId}")
    public String chat(Model model, @PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<ChatMessage> previousMessages = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        model.addAttribute("username", username);
        model.addAttribute("roomId", roomId);  // roomId도 모델에 추가
        model.addAttribute("previousMessages", previousMessages); // 이전 메시지를 모델에 추가
        return "chat";
    }

    // 메시지 보내기 (특정 채팅방에)
    @MessageMapping("/sendMessage/{roomId}")  // 클라이언트에서 /app/sendMessage/roomId로 메시지 보내면 이 메서드가 처리
    @SendTo("/topic/{roomId}")  // 응답 메시지가 /topic/roomId로 발송
    public ChatMessage sendMessage(ChatMessage message, @DestinationVariable Long roomId) {
        log.info("chatMessage = " + message.toString());
        chatMessageRepository.save(message); // 메시지 저장
        return message;
    }





}
