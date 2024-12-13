package est.commitdate.controller.memberController;

import est.commitdate.dto.memberDto.MemberSignUpRequest;
import est.commitdate.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지 반환
    @GetMapping("/sign-up")
    public String signUpForm() {
        return "view/signup";
    }

    // 회원가입 처리
    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute MemberSignUpRequest request) {
        memberService.signUp(request);
        return "redirect:/";// 회원가입 후 인덱스 페이지로 리디렉션
    }

    // 로그인 페이지 반환
    @GetMapping("/login")
    public String loginPage() {
        return "view/login";
    }

}
