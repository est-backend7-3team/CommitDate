package est.commitdate;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OAuthMemberSignUpTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("oAuthMemberSignUp")
    void oAuthMemberSignUp() throws Exception {
        try {
            Map<String, Object> attributes = Map.of(
                    "email", "test@test.com", "name", "OAuth Test User");

            OAuth2User oAuth2User = new DefaultOAuth2User(
                    null,
                    attributes, "email");// 이메일 기준으로 사용자 정보 확인

            // 회원가입 로직 동작
            String email = oAuth2User.getAttribute("email");
            Member member = memberRepository.findByEmail(email)
                    .orElseGet(() -> memberRepository.save(
                            Member.builder()
                                    .email(email)
                                    .username(oAuth2User.getAttribute("name"))
                                    .role("MEMBER")
                                    .provider("google")
                                    .nickname("Pending")
                                    .phoneNumber("Pending")
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .status(1)
                                    .build()
                            )
                    );

            // 검증
            Member savedMember = memberRepository.findByEmail(email).orElseThrow();
            assertThat(savedMember).isNotNull();
            assertThat(savedMember.getEmail()).isEqualTo(email);
            assertThat(savedMember.getNickname()).isEqualTo("Pending");
            assertThat(savedMember.getPhoneNumber()).isEqualTo("Pending");

            System.out.println("OAuth2.0 회원가입 테스트: 회원가입 성공");

        } catch (Exception e) {
            throw new Exception("OAuth2.0 회원가입 테스트 실패: " + e.getMessage());
        }

    }

}
