package est.commitdate.controller;

import est.commitdate.dto.MemberAdditionalInfo;
import est.commitdate.dto.OAuthSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class OAuthExtraInfoController {

    private final MemberRepository memberRepository;

    @GetMapping("/additional-info")
    public String extraInfoForm(Model model) {
        model.addAttribute("memberInfo", new MemberAdditionalInfo());
        return "/view/additional-info";
    }

    @PostMapping("/additional-info")
    public String saveAdditionalInfo(@ModelAttribute OAuthSignUpRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.setNickname(request.getNickname());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setUsername(request.getUsername());
        member.setAdditionalInfoCompleted(true);
        memberRepository.save(member);

        return "redirect:/";

    }

}