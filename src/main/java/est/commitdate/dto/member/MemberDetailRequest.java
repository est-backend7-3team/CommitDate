package est.commitdate.dto.member;

import est.commitdate.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailRequest {

    private String email;
    private String password;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String comment;
    private String introduce;

    public void applyToMember(Member member, String encryptedPassword) {
        member.updateMemberDetails(
                email,
                encryptedPassword != null ? encryptedPassword : member.getPassword(),
                nickname,
                phoneNumber,
                comment,
                introduce
        );
    }

}
