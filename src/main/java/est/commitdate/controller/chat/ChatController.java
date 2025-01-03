package est.commitdate.controller.chat;




import est.commitdate.dto.chat.ChatMessage;

import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.entity.ChatRoom;
import est.commitdate.entity.Member;
import est.commitdate.repository.ChatMessageRepository;
import est.commitdate.service.ChatService;
import est.commitdate.service.SwipeService;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final SwipeService swipeService;
    private final ChatService chatService;  // 메시지 저장소를 추
    private final MemberService memberService;

    @GetMapping("/chatting/{roomId}")
    public String chat(Model model, @PathVariable Long roomId, HttpSession session) {
        Member loggedInMember = memberService.getLoggedInMember(session);
        model.addAttribute("userImage", loggedInMember.getProfileImage());
        model.addAttribute("username", loggedInMember.getNickname());
        model.addAttribute("roomId", roomId);  // roomId도 모델에 추가
        model.addAttribute("previousMessages", chatService.previousMessages(roomId)); // 이전 메시지를 모델에 추가
        return "chat";
    }

    // 메시지 보내기 (특정 채팅방에)
    @MessageMapping("/sendMessage/{roomId}")  // 클라이언트에서 /app/sendMessage/roomId로 메시지 보내면 이 메서드가 처리
    @SendTo("/topic/{roomId}")  // 응답 메시지가 /topic/roomId로 발송
    public ChatMessage sendMessage(ChatMessage message, @DestinationVariable Long roomId) {
        log.info("chatMessage = " + message);
        ChatRoom findchatRoom = chatService.getChatRoom(roomId);
        // 메시지 저장
//        chatService.Chatting(message);
        chatService.Chatting(findchatRoom, message);
        return message;
    }


    // 채팅방 생성
    @ResponseBody
    @PostMapping("/chatroom/api/requestMatchingResult")
    public ResponseEntity<String> ChattingRoomMatching(@RequestBody ChooseDto chooseDto, HttpSession session) {
        chatService.ChatRoom(chooseDto.getPostId());
        log.info("채팅방 생성 호출 됨");
        return ResponseEntity.ok(swipeService.toggle(chooseDto, session));
    }

}
