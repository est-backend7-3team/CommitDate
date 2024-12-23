package est.commitdate.dto.member;

import lombok.Getter;
import lombok.Setter;
import est.commitdate.entity.Member;

@Getter
@Setter
public class MemberProfileRequest {

        private String email;
        private String password;
        private String nickname;
        private String phoneNumber;
        private String profileImage;
        private String comment;
        private String introduce;

        public static MemberProfileRequest fromMember(Member member) {
            MemberProfileRequest form = new MemberProfileRequest();
            form.setEmail(member.getEmail());
            form.setNickname(member.getNickname());
            form.setPhoneNumber(member.getPhoneNumber());
            form.setProfileImage(member.getProfileImage());
            form.setComment(member.getComment());
            form.setIntroduce(member.getIntroduce());
            return form;
        }

        // 비밀번호변경을 원하지 않는경우 그대로 두면 유지하도록함
        public void applyToMember(Member member, String encryptedPassword) {
            member.updateMemberDetails(
                    this.email,
                    (encryptedPassword != null) ? encryptedPassword : member.getPassword(),
                    this.nickname,
                    this.phoneNumber,
                    this.profileImage,
                    this.comment,
                    this.introduce
            );
        }
}