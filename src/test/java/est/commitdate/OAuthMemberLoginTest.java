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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OAuthMemberLoginTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("OAuth 회원가입 테스트")
    void oAuthMemberSignUp() throws Exception {

        Map<String, Object> attributes = Map.of(
                "email", "test@test.com",
                "name", "OAuth Test User"
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(
                null,
                attributes,
                "email"
        );

        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");


        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(email)
                        .username(username)
                        .role("MEMBER")
                        .nickname("Pending")
                        .phoneNumber("Pending")
                        .provider("google")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                );

        Member savedMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원가입 실패"));

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo(email);
        assertThat(savedMember.getUsername()).isEqualTo(username);
        assertThat(savedMember.getRole()).isEqualTo("MEMBER");

        System.out.println("OAuth 회원가입 테스트: 회원가입 성공");
    }

}
