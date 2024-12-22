package est.commitdate.dto.member;

import est.commitdate.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberSignUpRequest {

    private final String email;
    private final String username;
    private final String password;
    private final String nickname;
    private final String phoneNumber;

    @Builder
    public MemberSignUpRequest(String email, String username, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    // Member 엔티티 생성 메서드
    public Member toEntity(String encryptedPassword) {
        return new Member(
                email,
                null,
                "ADMIN", //
                username,
                nickname,
                phoneNumber,
                encryptedPassword,
                true, // 폼유저 기본값
                LocalDateTime.now(),
                LocalDateTime.now(),
                1
        );
    }
}