package est.commitdate.config;

import est.commitdate.dto.member.FormUserDetails;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TempPasswordChangeHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        Long memberId = null;
        // 현재 로그인한 유저의 id 조회
        if (principal instanceof FormUserDetails formUser) {
            memberId = formUser.getId();
        }

        Member member = memberRepository.findById(memberId).orElse(null);

        if (member != null && member.isTempPassword()) {
            // 임시 비번 상태라면, 프로필 페이지로 리디렉션
            response.sendRedirect("/member/profile");
        } else {
            // 그 외는 기본 페이지
            response.sendRedirect("/swipe");
        }
    }
}
