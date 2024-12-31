package est.commitdate.config;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        CustomUserDetails oAuthUser = (CustomUserDetails) authentication.getPrincipal();

        Optional<Member> member = memberRepository.findByEmail(oAuthUser.getEmail());

        if (member.isPresent()) {

            if (!member.get().isAdditionalInfoCompleted()) {
                // 추가 정보 입력이 필요한 경우
                response.sendRedirect("/additional-info");
            } else {
                // 추가 정보가 이미 존재하면 메인 페이지로 리디렉션
                response.sendRedirect("/swipe");
            }
        } else {
            log.info("등록된 회원이 없습니다!");
            response.sendRedirect("/login?error=true"); // 회원로그인이 안되어있는상태라면 로그인 페이지로 리디렉션
        }

    }

}
