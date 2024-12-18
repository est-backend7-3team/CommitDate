package est.commitdate.dto;


import est.commitdate.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String password;
    private String email;
    private String nickname;
    private String username;
    private String phoneNumber;
    private String role;
    private String profileImage;
    private String introduce;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int status;

    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getPassword(),
                member.getEmail(),
                member.getNickname(),
                member.getUsername(),
                member.getPhoneNumber(),
                member.getRole(),
                member.getProfileImage(),
                member.getIntroduce(),
                member.getComment(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getStatus()
        );
    }
}
