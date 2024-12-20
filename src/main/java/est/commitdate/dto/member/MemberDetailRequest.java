package est.commitdate.dto.member;

import est.commitdate.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDetailRequest {

    private String email;
    private String password;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String profileImage;
    private String comment;
    private String introduce;

    public void applyToMember(Member member, String encryptedPassword) {
        member.updateMemberDetails(
                email,
                encryptedPassword != null ? encryptedPassword : member.getPassword(),
                nickname,
                phoneNumber,
                profileImage,
                comment,
                introduce
        );
    }

}
