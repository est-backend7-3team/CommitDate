package est.commitdate.dto;

import lombok.Getter;
import est.commitdate.entity.Member;
import lombok.Setter;

@Getter
@Setter
public class MemberSignUpRequest {

    private String email;
    private String username;
    private String password;
    private String nickname;
    private String phoneNumber;

    public Member toEntity() {

        return Member.of(this);

    }


}