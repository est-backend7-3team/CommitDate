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
//        log.info(memberId.toString());
//        // id를 가져오지 못하면 로그인되지 않은것이므로 로그인페이지 반환
//        if (memberId == null) {
//            return "redirect:/login";
//        }
        // DB에서 회원 정보 조회 후 DTO 생성
        MemberProfileRequest profileRequest = memberService.getProfile(member.getId());
        model.addAttribute("memberProfileRequest", profileRequest);

        return "view/member/profile";
    }

    /*
    이부분이 어째서 Null 값으로 있는건지 확인이 필요함
    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal Object principal,
                                @ModelAttribute("memberProfileForm") MemberProfileRequest form) {
        Long memberId = getMemberIdFromPrincipal(principal);
     */

    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
                                @ModelAttribute("memberProfileRequest") MemberProfileRequest form) {
        Member member = memberService.getLoggedInMember(session);
        // id를 가져오지 못하면 로그인되지 않은것이므로 로그인페이지 반환
//        if (memberId == null) {
//            return "redirect:/login";
//        }
        memberService.updateProfile(member.getId(), form);

        return "redirect:/member/profile";
    }

}

