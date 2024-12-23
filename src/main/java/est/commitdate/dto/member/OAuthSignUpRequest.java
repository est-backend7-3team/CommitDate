package est.commitdate.dto.member;

import est.commitdate.entity.Member;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OAuthSignUpRequest {
    private final String email;
    private final String nickname;
    private final String phoneNumber;
    private final String username;

    public OAuthSignUpRequest(String email, String nickname, String phoneNumber, String username) {
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    // 엔티티 수정 메서드
    public void applyToMember(Member member) {
        member.updateAdditionalInfo(nickname, phoneNumber, username);
    }

    // 새로운 Member 엔티티 생성 메서드
    public Member toEntity(String provider) {
        return new Member(
                email,
                provider,
                "MEMBER",
                "Pending",
                "Pending",
                "Pending",
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1
        );
    }

}