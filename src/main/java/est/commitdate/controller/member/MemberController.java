package est.commitdate.controller.member;

import est.commitdate.dto.member.MemberDetailRequest;
import est.commitdate.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/delete")
    public String deleteMember(@RequestParam Long id) {
        memberService.delete(id);
        return "redirect:/logout"; // 탈퇴 후 로그아웃 처리
    }

    @PostMapping("/update")
    public String updateMemberDetails(@AuthenticationPrincipal User user, MemberDetailRequest request) {
        Long memberId = Long.valueOf(user.getUsername()); // Assuming username is ID
        memberService.updateMemberDetails(memberId, request);
        return "redirect:/member/detail";
    }

}