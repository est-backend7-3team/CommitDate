package est.commitdate.controller.member;

import est.commitdate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/forgot-password-request")
    public Map<String, Object> forgotPasswordRequest(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        Map<String, Object> result = new HashMap<>();

        if (email == null || email.isBlank()) {

        result.put("success", false);
        result.put("message", "이메일이 비어있습니다.");

        return result;
        }

        try {
        memberService.sendTemporaryPassword(email);

        result.put("success", true);
        result.put("message", "임시비밀번호가 발급되었습니다. 이메일을 확인하세요.");

        } catch (RuntimeException e) {

        // 서비스단의 errormsg에서 가져온 등록된 회원이 없다는 메세지 출력됨
        result.put("success", false);
        result.put("message", e.getMessage());
        }

        return result;
    }
}
