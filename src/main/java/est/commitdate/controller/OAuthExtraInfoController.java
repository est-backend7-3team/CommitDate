package est.commitdate.controller;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.member.MemberAdditionalInfo;
import est.commitdate.dto.member.OAuthSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    public String extraInfoForm(Model model, OAuth2AuthenticationToken authentication) {
        // 현재 로그인된 사용자 이메일 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        MemberAdditionalInfo memberInfo = new MemberAdditionalInfo();
        memberInfo.setEmail(userDetails.getEmail());

        model.addAttribute("memberInfo", memberInfo);
        return "/view/member/additional-info";
    }

    @PostMapping("/additional-info")
    public String saveAdditionalInfo(@ModelAttribute OAuthSignUpRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        request.applyToMember(member);
        memberRepository.save(member);

        return "redirect:/";

    }

}