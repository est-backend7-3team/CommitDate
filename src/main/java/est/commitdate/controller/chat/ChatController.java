package est.commitdate.controller.chat;




import est.commitdate.dto.chat.ChatMessage;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MemberService memberService;
    private final ChatMessage chatMessage;

    @GetMapping("/chatting")
    public String chatPage(Model model, HttpSession session) {
        model.addAttribute("username", memberService.getLoggedInMember(session).getNickname());
        return "chat"; // templates/chat.html 반환
    }

    // 클라이언트에서 app/chatMessage로 들어오는 메시지를 처리
    @MessageMapping("/send") // 클라이언트에서 발행된 메시지
    @SendTo("/topic/public") // 구독자들에게 전달할 경로
    public ChatMessage broadcastMessage(ChatMessage message) {
        return message; // 메시지를 그대로 반환
    }

}
