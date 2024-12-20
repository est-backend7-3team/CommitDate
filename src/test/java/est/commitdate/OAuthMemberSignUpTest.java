package est.commitdate;

import est.commitdate.dto.member.OAuthSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OAuthMemberSignUpTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("OAuth 회원가입 테스트")
    void oAuthMemberSignUp() {
        try {

            OAuthSignUpRequest request = new OAuthSignUpRequest(
                    "oauthuser@test.com",
                    "OAuth User",
                    "01087654321",
                    "OAuthTest"
            );
            Member member = request.toEntity("google");

            memberRepository.save(member);
            Member savedMember = memberRepository.findByEmail(request.getEmail()).orElse(null);

            // 검증
            assertThat(savedMember).isNotNull();
            assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
            System.out.println("OAuth 회원가입 테스트: 회원가입 성공");

        } catch (Exception e) {
            System.out.println("OAuth 회원가입 테스트 실패: " + e.getMessage());
        }

    }

}
