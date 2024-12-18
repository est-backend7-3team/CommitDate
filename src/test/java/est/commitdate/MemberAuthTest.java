package est.commitdate;

import est.commitdate.dto.MemberSignUpRequest;
import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import est.commitdate.dto.MemberSignUpRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberAuthTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberSignUpRequest request;

    @BeforeEach
    void setUp() {
        request = MemberSignUpRequest.builder()
                .email("testuser@test.com")
                .username("testuser")
                .password("password123")
                .nickname("tester")
                .phoneNumber("01012345678")
                .build();
    }

    @Test
    @DisplayName("testMemberSignUp")
    void testMemberSignUp() throws Exception {
        try {
            Member member = request.toEntity(passwordEncoder);

            memberRepository.save(member);

            memberRepository.save(member);

            // 저장된 회원 조회
            Member savedMember = memberRepository.findByEmail(request.getEmail()).orElseThrow();

            // 검증 부분
            assertThat(savedMember).isNotNull();
            assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
            assertThat(savedMember.getNickname()).isEqualTo(request.getNickname());
            assertThat(savedMember.getPhoneNumber()).isEqualTo(request.getPhoneNumber());

            System.out.println("회원가입 테스트: 회원가입 성공");

        } catch (Exception e) {
            throw new Exception("회원가입 테스트 실패: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("testMemberLogin")
    void testMemberLogin() throws Exception {
        try {
            // 조건 : 이미 회원가입이 되어있는 상태
            Member existingMember = memberRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new Exception("로그인 테스트 실패: 회원이 존재하지 않습니다."));

            // 비밀번호 확인
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), existingMember.getPassword());

            // 로그인 성공 검증
            if (!passwordMatches) {
                throw new Exception("로그인 테스트 실패: 비밀번호가 일치하지 않습니다.");
            }

            System.out.println("로그인 테스트: 로그인 성공");

        } catch (Exception e) {
            throw new Exception("로그인 테스트 실패: " + e.getMessage());
        }
    }

}
