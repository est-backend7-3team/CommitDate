package est.commitdate;


import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import est.commitdate.service.member.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DeleteMemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 탈퇴 테스트")
    @Transactional
    void testDeleteMember() throws Exception {
        // 회원 추가
        Member newMember = new Member(
                "testuser@test.com",
                null,
                "USER",
                "testuser",
                "tester",
                "01012345678",
                "adsfiatjhtoi",
                false,
                null,
                null,
                1
        );
        Member savedMember = memberRepository.save(newMember);
        Long memberId = savedMember.getId();

        // 회원 탈퇴
        memberService.delete(memberId);

//         assertThat(foundMember).isPresent();
        assertThat(savedMember.getStatus()).isEqualTo(0); // 상태가 0으로 변경되어야 함
        System.out.println("회원 탈퇴 테스트: 성공");
    }

}
