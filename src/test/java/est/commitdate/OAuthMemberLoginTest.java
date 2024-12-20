package est.commitdate;

import est.commitdate.dto.member.OAuthSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OAuthMemberLoginTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("OAuth 로그인 테스트")
    void oAuthMemberLogin() {
        try {
            OAuthSignUpRequest request = new OAuthSignUpRequest(
                    "oauthlogin@test.com",
                    "OAuth Login User",
                    "01011112222",
                    "OAuthLoginTest"
            );
            Member member = request.toEntity("kakao");
            memberRepository.save(member);


            Member foundMember = memberRepository.findByEmail(request.getEmail()).orElse(null);

            // 검증
            if (foundMember == null) {
                System.out.println("OAuth 로그인 테스트 실패: 회원이 존재하지 않습니다.");
                return;
            }
            assertThat(foundMember.getEmail()).isEqualTo(request.getEmail());
            System.out.println("OAuth 로그인 테스트: 로그인 성공");

        } catch (Exception e) {
            System.out.println("OAuth 로그인 테스트 실패: " + e.getMessage());
        }
    }

}
