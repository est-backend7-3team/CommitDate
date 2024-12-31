package est.commitdate;

import est.commitdate.dto.member.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberSignUpLoginTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberSignUpRequest request;

    @BeforeEach
    void setUp() {
        request = new MemberSignUpRequest(
                "testuser@test.com",
                "testuser",
                "password123",
                "tester",
                "01012345678"
        );
    }

    @Test
    @DisplayName("회원가입 테스트")
    void testMemberSignUp() {
        try {
            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            Member member = request.toEntity(encryptedPassword);

            memberRepository.save(member);
            Member savedMember = memberRepository.findByEmail(request.getEmail()).orElse(null);

            // 검증
            assertThat(savedMember).isNotNull();
            assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
            System.out.println("회원가입 테스트: 회원가입 성공");

        } catch (Exception e) {
            System.out.println("회원가입 테스트 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("로그인 테스트")
    void testMemberLogin() {
        try {
            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            Member member = request.toEntity(encryptedPassword);
            memberRepository.save(member);

            Member foundMember = memberRepository.findByEmail(request.getEmail()).orElse(null);

            // 검증
            if (foundMember == null) {
                System.out.println("로그인 테스트 실패: 회원이 존재하지 않습니다.");
                return;
            }

            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), foundMember.getPassword());
            assertThat(passwordMatches).isTrue();
            System.out.println("로그인 테스트: 로그인 성공");

        } catch (Exception e) {
            System.out.println("로그인 테스트 실패: " + e.getMessage());
        }

    }

}
