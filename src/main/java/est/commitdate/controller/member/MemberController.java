package est.commitdate.controller.member;

import est.commitdate.dto.member.MemberProfileRequest;
import est.commitdate.entity.Member;
import est.commitdate.service.SwipeService;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final SwipeService swipeService;

    @PostMapping("/delete")
    public String deleteMember(@RequestParam Long id) {
        memberService.delete(id);
        return "redirect:/logout"; // 탈퇴 후 로그아웃 처리
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session,
                              Model model) {
        Member member = memberService.getLoggedInMember(session);

        // DB에서 회원 정보 조회 후 DTO 생성
        MemberProfileRequest profileRequest = memberService.getProfile(member.getId());
        model.addAttribute("memberProfileRequest", profileRequest);
        model.addAttribute("originalNickname", member.getNickname());
        model.addAttribute("originalPhoneNumber", member.getPhoneNumber());

        return "view/member/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
                                @ModelAttribute("memberProfileRequest") MemberProfileRequest form) {
        Member member = memberService.getLoggedInMember(session);

        memberService.updateProfile(member.getId(), form);

        return "redirect:/member/profile";
    }

    @GetMapping("/forgot-password")
    public String findForgotPassword() {
        return "view/member/forgot-password";
    }

    // deprecated!!! RestController 로 대체했음.
//    @PostMapping("/forgot-password")
//    public String processForgotPassword(@RequestParam("email") String email) {
//        // email이 존재하는 계정인지 확인 후 임시비밀번호 발송
//        memberService.sendTemporaryPassword(email);
//
//        // 실행 후 로그인 페이지로 이동
//        return "redirect:/login";
//    }

}

